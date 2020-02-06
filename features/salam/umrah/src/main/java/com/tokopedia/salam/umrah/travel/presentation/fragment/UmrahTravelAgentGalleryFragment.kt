package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesEntity
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelGalleryAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelFragment.Companion.EXTRA_SLUGNAME
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelGalleryViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class UmrahTravelAgentGalleryFragment : BaseListFragment<UmrahGallery, UmrahTravelGalleryAdapterTypeFactory>(), BaseEmptyViewHolder.Callback{

    @Inject
    lateinit var umrahTravelGalleryViewModel: UmrahTravelGalleryViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    var galleries : UmrahGalleriesEntity = UmrahGalleriesEntity()

    private var slugName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = arguments?.getString(EXTRA_SLUGNAME, "")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): UmrahTravelGalleryAdapterTypeFactory = UmrahTravelGalleryAdapterTypeFactory(this)

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

    private fun onSuccessGetResult(data : List<UmrahGallery>){
        renderList(data,true)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_galleries


    private fun requestData(page:Int){
        slugName?.let {
            umrahTravelGalleryViewModel.getDataGallery(page, it,
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_travel_agent_gallery))
        }
    }
    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_gallery,container, false)

    companion object {
        fun createInstance(slugName: String) =
                UmrahTravelAgentGalleryFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUGNAME, slugName)
                    }
                }
    }
}