package com.tokopedia.home.beranda.presentation.view.adapter

//
//class HomeFeedAdapter : BaseListAdapter<HomeRecommendationVisitable, HomeRecommendationTypeFactory> {
//    val layoutList = listOf(
//            HomeFeedViewHolder.LAYOUT,
//            HomeBannerFeedViewHolder.LAYOUT
//    )
//
//    constructor(baseListAdapterTypeFactory: HomeRecommendationTypeFactory) : super(baseListAdapterTypeFactory) {}
//
//    constructor(baseListAdapterTypeFactory: HomeRecommendationTypeFactory, onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<Visitable<HomeFeedTypeFactory>>) : super(baseListAdapterTypeFactory, onAdapterInteractionListener) {}
//
//    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
//        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
//        layoutParams.isFullSpan = getItemViewType(position) !in layoutList
//        super.onBindViewHolder(holder, position)
//    }
//
//    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: List<Any>) {
//        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
//        layoutParams.isFullSpan = getItemViewType(position) !in layoutList
//        super.onBindViewHolder(holder, position, payloads)
//    }
//
//    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
//        super.onViewRecycled(holder)
//    }
//}
