package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.createpost.view.bottomSheet.ContentCreationProductTagBottomSheet
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.plist.ShopPageProduct
import com.tokopedia.createpost.view.posttag.ProductTaggingView
import com.tokopedia.createpost.view.viewmodel.*
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.content_creation_image_post.view.*
import kotlinx.android.synthetic.main.content_creation_video_post.*
import kotlinx.android.synthetic.main.content_creation_video_post.view.*
import kotlinx.android.synthetic.main.feed_preview_post_fragment_new.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

/**
 * @author by shruti on 30/08/21.
 */

class CreatePostPreviewFragmentNew : BaseCreatePostFragmentNew(), CreateContentPostCOmmonLIstener {


    private var videoPlayer: FeedExoPlayer? = null
    private var productVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    var isMute = true



    private lateinit var contentProductTagBS: ContentCreationProductTagBottomSheet

    private val productAdapter: RelatedProductAdapter by lazy {
        RelatedProductAdapter(null, RelatedProductAdapter.TYPE_PREVIEW)
    }

    private val imageList: ArrayList<String>
        get() = ArrayList(createPostModel.completeImageList.map { it.path })

    companion object {
        private const val REQUEST_ATTACH_PRODUCT = 10
        private const val PARAM_PRODUCT = "product"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = CreatePostPreviewFragmentNew()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feed_preview_post_fragment_new, container, false)
    }

    override fun fetchContentForm() {
        presenter.fetchContentForm(createPostModel.productIdList, createPostModel.authorType, createPostModel.postId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
    }

    private fun initVar() {
        createPostModel = arguments?.getParcelable(CreatePostViewModel.TAG) ?: CreatePostViewModel()

    }

    private fun initView() {
        val relatedProducts = ArrayList(createPostModel.relatedProducts)
        productAdapter.setList(relatedProducts)
        productAdapter.removeEmpty()
        createPostModel.maxProduct = 5
        image_position_text?.text = context?.let {
            String.format(
                it.getString(R.string.feed_content_position_text,
                    "(0/${createPostModel.maxProduct})")
            )
        }
        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]
        product_tag_button.setOnClickListener {
            setProductTagListener(mediaModel)
        }
        content_tag_product_text.setOnClickListener {
            setProductTagListener(mediaModel)
        }

        updateCarouselView()
        feed_content_carousel?.activeIndex = createPostModel.currentCorouselIndex
    }
    private fun setProductTagListener(mediaModel: MediaModel){
        if (getLatestTotalProductCount() < 5) {
            removeExtraTagListElement(mediaModel)
            val tagListSize = mediaModel.tags.size
            createPostModel.completeImageList[createPostModel.currentCorouselIndex].tags.add(
                FeedXMediaTagging(tagIndex = tagListSize, posX = 0.5f, posY = 0.5f))
            openProductTaggingScreen()
        }else{
            Toaster.build(requireView(),
                getString(R.string.feed_content_more_than_5_product_tag),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR).show()
        }

    }

    private fun removeExtraTagListElement(mediaModel: MediaModel){
        if (mediaModel.tags.size > mediaModel.products.size){
            val tagListSize = mediaModel.tags.size
            val extraSize = tagListSize - mediaModel.products.size
            for (i in 0 until extraSize) {
                createPostModel.completeImageList[createPostModel.currentCorouselIndex]
                    .tags.removeAt((tagListSize + i) - 1)
            }

        }

    }

    private fun updateResultIntent() {
        createContentPostViewModel.setNewContentData(createPostModel)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun updateCarouselView() {

        feed_content_carousel.apply {
            stage.removeAllViews()
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            if (imageList.size > 1) {
                page_indicator.show()
                page_indicator.setIndicator(imageList.size)
                page_indicator.indicatorCurrentPosition = activeIndex
            } else {
                page_indicator.hide()
            }

            createPostModel.completeImageList.forEachIndexed() { index, feedMedia ->

                if (feedMedia.type == MediaType.IMAGE) {
                    val products = feedMedia.products

                    val imageItem = View.inflate(context,
                        R.layout.content_creation_image_post,
                        null)
                    val param = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    imageItem.layoutParams = param
                    feedMedia.imageView = imageItem
                    imageItem.run {
                        findViewById<ImageUnify>(R.id.content_creation_post_image).setImageUrl(
                            imageList[index])
                        findViewById<CardView>(R.id.product_tagging_button_parent).showWithCondition(
                            products.isNotEmpty()
                        )
                        bindImage(feedMedia)


                        val gd = GestureDetector(
                            context,
                            object : GestureDetector.SimpleOnGestureListener() {
                                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                                    removeExtraTagListElement(feedMedia)
                                    val x = e?.x ?: 0L
                                    val y = e?.y ?: 0L
                                    val posX = round((x.toFloat() / imageItem.width) * 10) / 10
                                    val posY = round((y.toFloat() / imageItem.height) * 10) / 10
                                    val tagIndex = createPostModel.completeImageList[index].tags.size
                                    createPostModel.completeImageList[index].tags.add(FeedXMediaTagging(
                                        tagIndex = tagIndex,
                                        posX = posX,
                                        posY = posY))

                                    if (getLatestTotalProductCount() < 5)
                                        openProductTaggingScreen()
                                    return true
                                }

                                override fun onDown(e: MotionEvent): Boolean {
                                    return true
                                }

                                override fun onDoubleTap(e: MotionEvent): Boolean {
                                    return true
                                }

                                override fun onLongPress(e: MotionEvent) {
                                    super.onLongPress(e)
                                }

                                override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                                    return true
                                }
                            })

                        setOnTouchListener { v, event ->
                            gd.onTouchEvent(event)
                            true
                        }
                    }
                    addItem(imageItem)
                } else {

                    addItem(
                        setVideoCarouselView(
                            feedMedia, index)
                    )
                    if (index == 0)
                        playVideo(feedMedia, index)
                }

            }
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    createPostModel.currentCorouselIndex = current
                    page_indicator.setCurrentIndicator(current)

                    if (createPostModel.completeImageList[current].type == MediaType.VIDEO) {
                        playVideo(createPostModel.completeImageList[current],
                            current)
                    }

                }
            }
        }
    }

    private fun setVideoCarouselView(
        feedMedia: MediaModel,
        position: Int
    ): View {
        val videoItem = View.inflate(context, R.layout.content_creation_video_post, null)
        val param = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        videoItem?.layoutParams = param
        createPostModel.completeImageList[position].videoView = videoItem
        videoItem?.run {
            contentVideoPreviewImage?.setImageUrl(imageList[position])
            bindVideo(feedMedia)

        }
        return (videoItem)
    }

    fun playVideo(mediaModel: MediaModel, position: Int = feed_content_carousel.activeIndex) {
        setVideoControl(
            mediaModel,
            position
        )
    }


    private fun setVideoControl(
        mediaModel: MediaModel,
        index: Int
    ) {
        val videoItem = createPostModel.completeImageList[index].videoView
        createPostModel.completeImageList[index].isPlaying = true
        videoItem?.run {
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)
                feed_content_layout_video?.player = videoPlayer?.getExoPlayer()
                feed_content_layout_video?.videoSurfaceView?.setOnClickListener {
                    if (createPostModel.completeImageList[index].isPlaying) {
                        videoPlayer?.pause()
                        showVideoLoading()
                        createPostModel.completeImageList[index].isPlaying = false
                    } else {
                        hideVideoLoading()
                        videoPlayer?.replay()
                        createPostModel.completeImageList[index].isPlaying = true
                    }

                }
                content_product_tagging_parent?.setOnClickListener {
                    openBottomSheet(createPostModel.completeImageList[index].products)
                }

                videoPlayer?.start(mediaModel.path, isMute)
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                    }

                    override fun onVideoReadyToPlay() {

                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {

                    }
                })
            }
        }
    }


    private fun hideVideoLoading() {
        feed_content_ic_play?.gone()
    }

    private fun showVideoLoading() {
        feed_content_ic_play?.visible()
    }

    private fun toggleVolume(isMute: Boolean) {
        videoPlayer?.toggleVideoVolume(isMute)
    }

    private fun openBottomSheet(productList: List<RelatedProductItem>) {
        contentProductTagBS = ContentCreationProductTagBottomSheet()
        contentProductTagBS.show(Bundle.EMPTY,
            childFragmentManager,
            productList,
            this)

    }

    override fun deleteItemFromProductTagList(position: Int, isDeletedFromBubble: Boolean) {

        val currentImagePos = createPostModel.currentCorouselIndex
        removeExtraTagListElement(createPostModel.completeImageList[currentImagePos])
        try {
            createPostModel.completeImageList[currentImagePos].products.removeAt(position)
            createPostModel.completeImageList[currentImagePos].tags.removeAt(position)
        } catch (e: Exception) {
            Timber.e(e)
        }
        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"
        image_position_text.text = String.format(
            requireContext().getString(R.string.feed_content_position_text),
            pos
        )

        //update tagIndex
        createPostModel.completeImageList[currentImagePos].tags.forEachIndexed { index, feedXMediaTagging ->
            feedXMediaTagging.tagIndex = index
        }

        if (createPostModel.completeImageList[currentImagePos].products.size == 0) {
            if (::contentProductTagBS.isInitialized)
                contentProductTagBS.dismiss()

        }
        val mediaModel = createPostModel.completeImageList[currentImagePos]
        if (getLatestTotalProductCount() < 5)
            enableProductIcon()

        if (mediaModel.type == MediaType.VIDEO)
            bindVideo(mediaModel)
        else
            bindImage(mediaModel)

        updateResultIntent()

        if (!isDeletedFromBubble)
            Toaster.build(requireView(),
                getString(R.string.feed_content_delete_toaster_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL).show()

    }

    override fun updateHeader(header: HeaderViewModel) {
        TODO("Not yet implemented")
    }

    override fun launchProductTagFragment(data: ArrayList<Uri>?) {
        TODO("Not yet implemented")
    }

    override fun openProductTagginPageOnPreviewMediaClick(position: Int) {
        TODO("Not yet implemented")
    }


    private fun openProductTaggingScreen() {
        goToAttachProduct()

    }

    private fun goToAttachProduct() {
            activity?.let{
                val intent = RouteManager.getIntent(context, "tokopedia://productpickerfromshop?shopid=${userSession.shopId}&source=shop_product")
                startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ATTACH_PRODUCT -> if (resultCode == Activity.RESULT_OK) {
                getAttachProductResult(data)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun getAttachProductResult(data: Intent?) {
        val product = data?.getSerializableExtra(
            PARAM_PRODUCT) as ShopPageProduct

        product.let {
            createPostModel.productIdList.add(it.pId!!)
            val relatedProductItem = mapResultToRelatedProductItem(it)
            createPostModel.completeImageList[createPostModel.currentCorouselIndex].products.add(
                relatedProductItem)
        }
        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]
        removeExtraTagListElement(mediaModel)

        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"
        image_position_text.text = String.format(
            requireContext().getString(R.string.feed_content_position_text),
            pos
        )

        if (getLatestTotalProductCount() == 5)
            disableProductIcon()

        if (mediaModel.products.size > 0) {
            if (mediaModel.type == MediaType.VIDEO)
                bindVideo(mediaModel)
            else
                bindImage(mediaModel)
        }
        updateResultIntent()
    }

    private fun mapResultToRelatedProductItem(item: ShopPageProduct): RelatedProductItem {
        return RelatedProductItem(
            id = item.pId!!,
            name = item.name!!,
            price = item.price?.priceIdr!!,
            image = item.pImage?.img!!,
            priceOriginalFmt = item.campaign?.oPriceFormatted!!,
            priceDiscountFmt = item.campaign.dPriceFormatted,
            isDiscount = (item.campaign.dPrice.toInt() != 0)
        )
    }
    private fun getLatestTotalProductCount() : Int{
        var count = 0
        createPostModel.completeImageList.forEach { mediaModel ->
            count += mediaModel.products.size
        }
        return count
    }
    private fun disableProductIcon(){
        val color = context?.let { ContextCompat.getColor(it, R.color.Unify_NN300 ) }
        color?.let {
            product_tag_button.setColorFilter(it)
            content_tag_product_text.setTextColor(it)
        }

    }
    private fun enableProductIcon(){
        val color = context?.let { ContextCompat.getColor(it, R.color.Unify_NN900 ) }
        color?.let {
            product_tag_button.setColorFilter(it)
            content_tag_product_text.setTextColor(it)
        }
    }

    private fun bindImage(media: MediaModel) {
        val products = media.products
        val imageItem = media.imageView

        removeExtraTagListElement(media)

        imageItem?.run {
            val lihatProductTagView = findViewById<CardView>(R.id.product_tagging_button_parent)
            lihatProductTagView.showWithCondition(products.isNotEmpty())

            lihatProductTagView.setOnClickListener {
                openBottomSheet(createPostModel.completeImageList[createPostModel.currentCorouselIndex].products)
            }
            val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
            val childCount = layout?.childCount ?: 0
                for (i in 0 until childCount) {
                    val view = layout.getChildAt(i)
                    if (view is ProductTaggingView) {
                        view.hideExpandedView()
                        layout.removeView(view)
                    }
                }

                media.tags.forEachIndexed { index, feedXMediaTagging ->
                    val productTagView = ProductTaggingView(context, feedXMediaTagging)
                    productTagView.bindData(this@CreatePostPreviewFragmentNew,
                        products,
                        width,
                        height,
                        index)
                    layout.addView(productTagView)
                }
        }
    }
    private fun bindVideo(mediaModel: MediaModel) {
        mediaModel.videoView?.run {
            content_product_tagging_parent.showWithCondition(mediaModel.products.isNotEmpty())
            content_product_tagging_parent?.setOnClickListener {
                openBottomSheet(createPostModel.completeImageList[createPostModel.currentCorouselIndex].products)
            }

        }

    }
}

