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
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent
import com.tokopedia.kol.feature.post.di.KolProfileModule
import com.tokopedia.kol.feature.postdetail.view.adapter.CDPRevampAdapter
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.CDPPostViewHolder
import com.tokopedia.kol.feature.postdetail.view.datamodel.CDPRevampDataUiModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.CPDRevampViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by shruti agarwal on 15/06/22
 */

class CDPRevampedFragment : BaseDaggerFragment() , CDPPostViewHolder.CDPListener{

    private var cdpRecyclerView: RecyclerView? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private val adapter = CDPRevampAdapter(
        dataSource = object : CDPRevampAdapter.DataSource {
            override fun getData(): CDPRevampDataUiModel {
                TODO("Not yet implemented")
            }

            override fun getPositionInFeed(): Int {
              return 0
            }

        },
        cdpListener = this
    )


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val cdpViewModel: CPDRevampViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(CPDRevampViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle?): CDPRevampedFragment {
            val fragment = CDPRevampedFragment()
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

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        cdpViewModel.run {
            getCDPPostFirstPostData.observe(viewLifecycleOwner, Observer {
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
                        onSuccessGetCDPRecomData(CDPRevampDataUiModel(postList = it.data.posts))
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
        return inflater.inflate(R.layout.fragment_cdp_revamp_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cdpRecyclerView = view.findViewById(R.id.cdp_recycler_view)
        setupView(view)
        cdpViewModel.getCDPPostDetailFirstData("148176392")

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
                //TODO
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

            }
        }
    }

    private fun onSuccessGetFirstPostCDPData(cdpRevampDataUiModel: CDPRevampDataUiModel){
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(cdpViewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(cdpRevampDataUiModel.postList)
        cdpViewModel.getCDPRecomData(id = "148176392")

    }
    private fun onSuccessGetCDPRecomData(cdpRevampDataUiModel: CDPRevampDataUiModel){
        adapter.addItemsAndAnimateChanges(cdpRevampDataUiModel.postList)

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
        if (activity != null && activity?.application != null) {
            DaggerKolProfileComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(activity?.application))
                    .kolProfileModule(KolProfileModule())
                    .build()
                    .inject(this)
        }
    }
}