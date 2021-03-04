package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesInput
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import com.tokopedia.salam.umrah.travel.data.UmrahGalleryImageMapper
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahYoutubePlayerActivity
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelGalleryAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryOneImageViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryThreeImageViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryVideoViewHolder
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelFragment.Companion.EXTRA_SLUGNAME
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelGalleryViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_gallery.*
import javax.inject.Inject

class UmrahTravelAgentGalleryFragment : BaseListFragment<UmrahGallery, UmrahTravelGalleryAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback,
        UmrahTravelAgentGalleryThreeImageViewHolder.SetOnClickListener,
        UmrahTravelAgentGalleryOneImageViewHolder.SetOnClickListener,
        UmrahTravelAgentGalleryVideoViewHolder.OnYoutubeClick {

    @Inject
    lateinit var umrahTravelGalleryViewModel: UmrahTravelGalleryViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics


    @Inject
    lateinit var umrahTracking: UmrahTrackingAnalytics


    var galleries: List<UmrahGallery> = emptyList()
    var galleriesParam: UmrahGalleriesInput = UmrahGalleriesInput()

    private var slugName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = arguments?.getString(EXTRA_SLUGNAME, "")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): UmrahTravelGalleryAdapterTypeFactory = UmrahTravelGalleryAdapterTypeFactory(this, this, this, this)

    override fun loadData(page: Int) {
        requestData(page)
    }

    override fun onEmptyButtonClicked() {

    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(t: UmrahGallery?) {

    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.umrah_img_empty_search_png
        emptyModel.title = getString(R.string.umrah_travel_empty_gallery_title)
        emptyModel.content = getString(R.string.umrah_travel_empty_gallery_sub_title)
        return emptyModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahTravelGalleryViewModel.galleryResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetResult(it.data.umrahGalleries)
                }
                is Fail -> {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, null, null, null, R.drawable.img_umrah_pdp_empty_state) {
                        loadInitialData()
                    }
                }

            }
        })
    }

    override fun onPlayYoutube(gallery: UmrahGallery,url: String, positionAdapter: Int, positionVideo:Int) {
        umrahTrackingUtil.umrahTravelAgentGalleryClicked(gallery, positionVideo)
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view?.context?.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                it.startActivity(UmrahYoutubePlayerActivity.createIntent(it, url))
            } else {
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.umrah_youtube_link,url))))
            }
        }
    }


    private fun onSuccessGetResult(data: List<UmrahGallery>) {
        galleries = data
        rv_umrah_travel_galleries.apply{
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && userVisibleHint) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(userVisibleHint) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }
            })
        }
        renderList(data, true)
    }

    fun firstTracking(){
        val layoutManager = rv_umrah_travel_galleries.layoutManager
        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        val dataList = (adapter as BaseListAdapter<*, *>).data
        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
    }

    private fun trackImpression(startIndex: Int, lastIndex: Int, data: MutableList<out Any>) {
        for (i in startIndex..lastIndex) {
            if (i < data.size) {
                if (data[i] is UmrahGallery) {
                    val gallery = data[i] as UmrahGallery
                    if (!gallery.isViewed) {
                        umrahTracking.umrahTravelAgentImpressionGallery(gallery)
                        gallery.isViewed = true
                    }
                }
            }
        }
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_galleries

    private fun requestData(page: Int) {
        slugName?.let {
            umrahTravelGalleryViewModel.getDataGallery(page, it,
                    UmrahQuery.UMRAH_TRAVEL_AGENT_GALLERY)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_gallery, container, false)

    companion object {
        fun createInstance(slugName: String) =
                UmrahTravelAgentGalleryFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUGNAME, slugName)
                    }
                }
    }

    override fun onClickThreeImage(gallery: UmrahGallery,positionImage:Int) {
        umrahTrackingUtil.umrahTravelAgentGalleryClicked(gallery, positionImage)
        showImagePreview(gallery,positionImage)
    }

    override fun onClickOneImage(gallery: UmrahGallery, positionImage:Int) {
        umrahTrackingUtil.umrahTravelAgentGalleryClicked(gallery, positionImage)
        showImagePreview(gallery,positionImage)
    }

    private fun showImagePreview(gallery: UmrahGallery,positionImage:Int) {
        val mappedSource = UmrahGalleryImageMapper.galleryImageSource(gallery.medias)
        val mappedThumbail = UmrahGalleryImageMapper.galleryImageThumbnail(gallery.medias)
        context?.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    this, getString(R.string.umrah_home_page_partner_label), mappedSource, mappedThumbail, positionImage
            ))
        }
    }
}