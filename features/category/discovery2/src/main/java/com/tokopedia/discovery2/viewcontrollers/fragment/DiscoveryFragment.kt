package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DiscoveryFragment : Fragment(), RecyclerView.OnChildAttachStateChangeListener {
    private lateinit var mDiscoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mPageComponentRecyclerView: RecyclerView
    private lateinit var typographyHeader: Typography
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var globalError: GlobalError
    var pageEndPoint = ""


    companion object {
        fun getInstance(endPoint: String?): Fragment {
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            if (!endPoint.isNullOrEmpty()) {
                bundle.putString(END_POINT, endPoint)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discovery, container, false)
        initView(view)
        mDiscoveryViewModel = (activity as DiscoveryActivity).getViewModel()
        mDiscoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        pageEndPoint = mDiscoveryViewModel.pageIdentifier
        return view
    }

    private fun initView(view: View) {
        mDiscoveryFab = view.findViewById(R.id.fab)
        typographyHeader = view.findViewById(R.id.typography_header)
        ivShare = view.findViewById(R.id.iv_share)
        ivSearch = view.findViewById(R.id.iv_search)
        globalError = view.findViewById(R.id.global_error)
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener { activity?.onBackPressed() }
        mPageComponentRecyclerView = view.findViewById(R.id.discovery_recyclerView)
        mPageComponentRecyclerView.layoutManager = LinearLayoutManager(activity)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(this)
        mPageComponentRecyclerView.adapter = mDiscoveryRecycleAdapter
        mPageComponentRecyclerView.addOnChildAttachStateChangeListener(this)
    }

    override fun onDetach() {
        mPageComponentRecyclerView.removeOnChildAttachStateChangeListener(this)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDiscoveryViewModel.getDiscoveryData()
        setUpObserver()
    }

    private fun setUpObserver() {
        mDiscoveryViewModel.getDiscoveryResponseList().observe(this, Observer {
            when (it) {
                is Success -> {
                    mDiscoveryRecycleAdapter.setDataList(it.data)
                }
            }
        })

        mDiscoveryViewModel.getDiscoveryFabLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    it.data.data?.get(0)?.let { data ->
                        setFloatingActionButton(data)
                        setAnimationOnScroll()
                    }
                }

                is Fail -> {
                    mDiscoveryFab.hide()
                }
            }
        })

        mDiscoveryViewModel.getDiscoveryPageInfo().observe(this, Observer {
            when (it) {
                is Success -> {
                    ivSearch.show()
                    setPageInfo(it.data)
                }

                is Fail -> {
                    typographyHeader.text = getString(R.string.discovery)
                    ivSearch.hide()
                    ivShare.hide()

                    if (it.throwable is UnknownHostException
                            || it.throwable is SocketTimeoutException) {
                        globalError.setType(GlobalError.NO_CONNECTION)
                    } else {
                        globalError.setType(GlobalError.SERVER_ERROR)
                    }

                    globalError.show()



                }
            }
        })
    }

    private fun setPageInfo(data: PageInfo?) {
        typographyHeader.text = data?.name
        if (data?.share?.enabled == true) {
            ivShare.show()
            ivShare.setOnClickListener {
                Utils.shareData(activity, data.share.description, data.share.url, mDiscoveryViewModel.getBitmapFromURL(data.share.image))
            }
            ivSearch.setOnClickListener {
                if(data.searchApplink?.isNotEmpty() == true) {
                    RouteManager.route(context, data.searchApplink)
                } else {
                    RouteManager.route(context, Utils.SEARCH_DEEPLINK)
                }
            }
        } else {
            ivShare.hide()
        }

    }

    private fun setAnimationOnScroll() {
        mPageComponentRecyclerView.addOnScrollListener(mDiscoveryFab.getScrollListener())
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        (mPageComponentRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewDetachedToWindow()
    }

    override fun onChildViewAttachedToWindow(view: View) {
        (mPageComponentRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewAttachedToWindow()
    }

    private fun setFloatingActionButton(data: DataItem) {
        mDiscoveryFab.apply {
            show()
            showTextAnimation(data)
            data.thumbnailUrlMobile?.let { showImageOnFab(context, it) }
            setClick(data.applinks?.toEmptyStringIfNull().toString(), data.shopId?.toIntOrNull()
                    ?: 0)
        }
    }

    private fun setClick(appLinks: String, shopId: Int) {
        mDiscoveryFab.getFabButton().setOnClickListener {
            if (appLinks.isNotEmpty() && shopId != 0) {
                activity?.let { it1 -> mDiscoveryViewModel.openCustomTopChat(it1, appLinks, shopId) }
            }
        }
    }
}