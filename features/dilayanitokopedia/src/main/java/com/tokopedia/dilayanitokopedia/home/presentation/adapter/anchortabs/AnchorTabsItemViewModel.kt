package com.tokopedia.dilayanitokopedia.home.presentation.adapter.anchortabs

//class AnchorTabsItemViewModel(
//    val application: Application,
//    val components: ComponentsItem,
//    val position: Int
//) : DiscoveryBaseViewModel() {
//
//    @Inject
//    lateinit var anchorTabsUseCase: AnchorTabsUseCase
//    private var dataItem: DataItem? = null
//
//    init {
//        dataItem = components.data?.firstOrNull()
//    }
//
//    override fun onAttachToViewHolder() {
//        super.onAttachToViewHolder()
//        components.shouldRefreshComponent = null
//    }
//
//    fun getTitle(): String {
//        return dataItem?.name ?: ""
//    }
//
//    fun getImageUrl(): String {
//        return dataItem?.imageUrlMobile ?: ""
//    }
//
//    fun isSelected(): Boolean {
//        return (dataItem?.targetSectionID == anchorTabsUseCase.selectedId)
//    }
//
//    fun getSectionID(): String {
//        return dataItem?.targetSectionID ?: ""
//    }
//
//    fun getImageURLForView(isHorizontalTab: Boolean, shouldShowIcon: Boolean):String {
//        return if(isHorizontalTab || shouldShowIcon)
//            getImageUrl()
//        else ""
//    }
//
//    fun parentPosition(): Int {
//        return getComponent(
//            componentId = components.parentComponentId,
//            components.pageEndPoint
//        )?.position ?: components.parentComponentPosition
//    }
//}
