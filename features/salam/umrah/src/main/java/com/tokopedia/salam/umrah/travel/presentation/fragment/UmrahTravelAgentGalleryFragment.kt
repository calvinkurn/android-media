package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesEntity
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
        umrahTravelGalleryViewModel.galleryResult.observe(this, Observer {
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

    override fun onPlayYoutube(url: String) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view?.context?.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                it.startActivity(UmrahYoutubePlayerActivity.createIntent(it, url))
            } else {
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + url)))
            }
        }
    }

    private fun onSuccessGetResult(data: List<UmrahGallery>) {
        galleries = data
        renderList(data, data.size >= galleriesParam.limit)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_galleries


    private fun requestData(page: Int) {
        slugName?.let {
            umrahTravelGalleryViewModel.getDataGallery(page, it,
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_travel_agent_gallery))
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

    override fun onClickThreeImage(position: Int) {
        showImagePreview(position)
    }

    override fun onClickOneImage(position: Int) {
        showImagePreview(position)
    }

    private fun showImagePreview(position: Int) {
        val mappedSource = UmrahGalleryImageMapper.galleryImageSource(galleries[position].medias)
        val mappedThumbail = UmrahGalleryImageMapper.galleryImageThumbnail(galleries[position].medias)
        context?.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    this, getString(R.string.umrah_home_page_partner_label), mappedSource, mappedThumbail, position
            ))
        }
    }
}