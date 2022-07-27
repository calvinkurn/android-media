package com.tokopedia.kol.feature.postdetail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent
import com.tokopedia.kol.feature.post.di.KolProfileModule
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity
import com.tokopedia.kol.feature.postdetail.view.adapter.ContentDetailPageRevampAdapter
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampDataUiModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailRevampViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by shruti agarwal on 15/06/22
 */

class ContentDetailPageRevampedFragment : BaseDaggerFragment() , ContentDetailPostViewHolder.CDPListener{

    private var cdpRecyclerView: RecyclerView? = null
    private var postId = "0"
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private val adapter = ContentDetailPageRevampAdapter(
        dataSource = object : ContentDetailPageRevampAdapter.DataSource {
            override fun getData(): FeedXCard {
                TODO("Not yet implemented")
            }

            override fun getPositionInFeed(): Int {
              return 0
            }

        },
        contentDetailListener = this
    )


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val cdpViewModel: ContentDetailRevampViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(ContentDetailRevampViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle?): ContentDetailPageRevampedFragment {
            val fragment = ContentDetailPageRevampedFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }

    private fun initVar() {
        postId = arguments?.getString(KolPostDetailActivity.PARAM_POST_ID) ?: "0"
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        cdpViewModel.run {
            getContentDetailPostFirstPostData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        //TODO add checks
                        onSuccessGetFirstPostCDPData(it.data)
                    }
                    else -> {
                        //TODO handle
                        showToast(getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder), Toaster.TYPE_ERROR)
                    }
                }
            })
            cDPPostRecomData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetCDPRecomData(ContentDetailRevampDataUiModel(postList = it.data.posts))
                    }
                    else -> {
                        //TODO handle
                        showToast(getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder), Toaster.TYPE_ERROR)
                    }
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content_detail_revamp_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cdpRecyclerView = view.findViewById(R.id.cdp_recycler_view)
        setupView(view)
        cdpViewModel.getCDPPostDetailFirstData(postId)

    }
    private fun setupView(view: View) {
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            cdpRecyclerView?.addOnScrollListener(it)
            it.resetState()
        }
        cdpRecyclerView?.adapter = adapter
    }
    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(cdpRecyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                cdpViewModel.getCDPRecomData(postId)
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                FeedScrollListenerNew.onCDPScrolled(
                                    recyclerView,
                                    adapter.getList()
                                )
                            }
                        }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        }
    }

    private fun onSuccessGetFirstPostCDPData(contentDetailRevampDataUiModel: ContentDetailRevampDataUiModel){
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(cdpViewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(contentDetailRevampDataUiModel.postList)
        cdpViewModel.getCDPRecomData(postId)

    }
    private fun onSuccessGetCDPRecomData(contentDetailRevampDataUiModel: ContentDetailRevampDataUiModel){
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(cdpViewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(contentDetailRevampDataUiModel.postList)

    }
    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false)
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()

        }
    }


    override fun getScreenName(): String {
        return "CDP Revamp Fragment"
    }

    override fun initInjector() {
        activity?.application?.let {
            DaggerKolProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity?.application))
                .kolProfileModule(KolProfileModule())
                .build()
                .inject(this)
        }

    }
}