package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.activity.CreatePostActivityNew
import com.tokopedia.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.createpost.view.bottomSheet.ContentCreationProductTagBottomSheet
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.createpost.view.posttag.TagViewProvider
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker_insta.common.ui.menu.MenuManager
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.createpost.view.activity.ProductTagActivity
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Runnable
import kotlin.math.round

/**
 * @author by shruti on 30/08/21.
 */

class CreatePostPreviewFragmentNew : BaseCreatePostFragmentNew(), CreateContentPostCommonListener {

    /** View */
    private lateinit var tvImagePosition: Typography
    private lateinit var icProductTag: IconUnify
    private lateinit var tvContentTagProduct: Typography
    private lateinit var feedContentCarousel: CarouselUnify
    private lateinit var pageIndicatorView: PageControl

    /** View Video Post */
    private lateinit var imgContentVideoPreview: ImageUnify
    private lateinit var pvContentVideo: PlayerView
    private lateinit var cvProductTaggingParent: CardView
    private lateinit var ivPlayContent: ImageView

    private var videoPlayer: FeedExoPlayer? = null
    private var productVideoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    var isMute = true


    private val productAdapter: RelatedProductAdapter by lazy {
        RelatedProductAdapter(null, RelatedProductAdapter.TYPE_PREVIEW)
    }

    private val imageList: ArrayList<String>
        get() = ArrayList(createPostModel.completeImageList.map { it.path })

    companion object {
        private const val REQUEST_ATTACH_PRODUCT = 10
        private const val PARAM_PRODUCT = "product" /** TODO: gonna delete it soon */
        private const val PARAM_SHOP_NAME = "shop_name"
        private const val PARAM_SHOP_BADGE = "shop_badge"
        private const val PARAM_PRODUCT_TAG_SOURCE = "product_tag_source"
        private const val PARAM_AUTHOR_ID = "author_id"
        private const val PARAM_AUTHOR_TYPE = "author_type"
        private const val MAX_PRODUCT_TAG = 5
        private const val DEFAULT_DELAY = 50L

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
        setHasOptionsMenu(true)
        initVar()
        initView(view)
    }

    private fun initVar() {
        createPostModel = arguments?.getParcelable(CreatePostViewModel.TAG) ?: CreatePostViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuTitle =  activity?.getString(com.tokopedia.content.common.R.string.feed_content_text_lanjut)
        if(!menuTitle.isNullOrEmpty()) {
            MenuManager.addCustomMenu(activity, menuTitle, true, menu) {
                GlobalScope.launchCatchError(Dispatchers.IO, block = {
                    setMediaWidthAndHeight()
                }) { Timber.d(it) }

                activityListener?.clickContinueOnTaggingPage()
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MenuManager.MENU_ITEM_ID -> {
                GlobalScope.launchCatchError(Dispatchers.IO, block = {
                    setMediaWidthAndHeight()
                }) { Timber.d(it) }
                activityListener?.clickContinueOnTaggingPage()
                return true
            }
        }
        return false
    }

    private fun initView(view: View) {
        tvImagePosition = view.findViewById(R.id.image_position_text)
        icProductTag = view.findViewById(R.id.product_tag_button)
        tvContentTagProduct = view.findViewById(R.id.content_tag_product_text)
        feedContentCarousel = view.findViewById(R.id.feed_content_carousel)
        pageIndicatorView = view.findViewById(R.id.page_indicator)

        val relatedProducts = ArrayList(createPostModel.relatedProducts)
        productAdapter.setList(relatedProducts)
        productAdapter.removeEmpty()

        createPostModel.maxProduct = MAX_PRODUCT_TAG
        updateTotalProductTaggedText()

        if (getLatestTotalProductCount() == MAX_PRODUCT_TAG)
            disableProductIcon()
        else
            enableProductIcon()

        icProductTag.setOnClickListener {
            setProductTagListener()
        }
        tvContentTagProduct.setOnClickListener {
            setProductTagListener()
        }

        updateCarouselView()
        feedContentCarousel.activeIndex = createPostModel.currentCorouselIndex
    }

    private fun setProductTagListener(){
        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]

        createPostAnalytics.eventClickTagProductIcon(mediaModel.type)

        if (getLatestTotalProductCount() < MAX_PRODUCT_TAG) {
            removeExtraTagListElement(mediaModel)
            val tagListSize = mediaModel.tags.size
            val imageWidth = ((mediaModel.imageView?.width))?.toFloat() ?: 0f
            val imageHeight = ((mediaModel.imageView?.height))?.toFloat() ?: 0f
            createPostModel.completeImageList[createPostModel.currentCorouselIndex].tags.add(
                FeedXMediaTagging(tagIndex = tagListSize,
                    posX = 0.5f,
                    posY = 0.5f,
                    X = imageWidth/ 2,
                    Y = imageHeight / 2,
                mediaIndex = createPostModel.currentCorouselIndex)
            )
            openProductTaggingScreen()
        } else {
            Toaster.build(requireView(),
                getString(com.tokopedia.content.common.R.string.feed_content_more_than_5_product_tag),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR).show()
        }
    }

    private fun removeExtraTagListElement(mediaModel: MediaModel) {
        if (mediaModel.tags.size > mediaModel.products.size) {
            val tagListSize = mediaModel.tags.size
            val extraSize = tagListSize - mediaModel.products.size
            for (i in 0 until extraSize) {
                val currentPostTags = createPostModel.completeImageList[createPostModel.currentCorouselIndex].tags
                val index = (tagListSize + i) - 1
                if (index in 0 until currentPostTags.size) {
                    createPostModel.completeImageList[createPostModel.currentCorouselIndex]
                        .tags.removeAt(index)
                }
            }
        }
    }

    private fun updateResultIntent() {
        createContentPostViewModel.setNewContentData(createPostModel)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun updateCarouselView() {

        feedContentCarousel.apply {
            stage.removeAllViews()
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            if (imageList.size > 1) {
                pageIndicatorView.show()
                pageIndicatorView.setIndicator(imageList.size)
                pageIndicatorView.indicatorCurrentPosition = activeIndex
            } else {
                pageIndicatorView.hide()
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
                        val postImage = findViewById<ImageUnify>(R.id.content_creation_post_image)
                        postImage.setImageUrl(imageList[index])
                        findViewById<CardView>(R.id.product_tagging_button_parent).showWithCondition(
                            products.isNotEmpty()
                        )
                        val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
                        bindImage(feedMedia, index)

                        val gd = GestureDetector(
                            context,
                            object : GestureDetector.SimpleOnGestureListener() {
                                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                                        val bitmap = postImage.drawable.toBitmap()
                                        val greyX = calculateGreyAreaY(layout, bitmap)
                                        val greyY = calculateGreyAreaX(layout, bitmap)

                                    if (e?.x!! < greyX || e?.x > (layout.width - greyX) || e?.y!! < greyY || e?.y > (layout.height - greyY))
                                        return false

                                    createPostAnalytics.eventClickOnImageToTag(feedMedia.type)
                                    removeExtraTagListElement(feedMedia)

                                    val x = e?.x ?: 0L
                                    val y = e?.y ?: 0L
                                    val posX = round((x.toFloat() / imageItem.width) * 1000) / 1000
                                    val posY = round((y.toFloat() / imageItem.height) * 1000) / 1000
                                    val tagIndex = createPostModel.completeImageList[index].tags.size
                                    createPostModel.completeImageList[index].tags.add(
                                        FeedXMediaTagging(
                                            tagIndex = tagIndex,
                                            posX = posX,
                                            posY = posY,
                                            X = e?.x,
                                            Y = e?.y,
                                            rawX = e?.rawX,
                                            rawY = e?.rawY,
                                            mediaIndex = createPostModel.currentCorouselIndex)
                                    )

                                    if (getLatestTotalProductCount() < MAX_PRODUCT_TAG)
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
                    if (createPostModel.completeImageList[createPostModel.currentCorouselIndex].type == MediaType.VIDEO)
                        createPostModel.completeImageList[createPostModel.currentCorouselIndex].isPlaying =
                            false
                    createPostModel.currentCorouselIndex = current
                    pageIndicatorView.setCurrentIndicator(current)

                    if (createPostModel.completeImageList[current].type == MediaType.VIDEO) {
                        detach()
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

        imgContentVideoPreview = videoItem.findViewById(R.id.contentVideoPreviewImage)
        pvContentVideo = videoItem.findViewById(R.id.feed_content_layout_video)
        cvProductTaggingParent = videoItem.findViewById(R.id.content_product_tagging_parent)
        ivPlayContent = videoItem.findViewById(R.id.feed_content_ic_play)

        createPostModel.completeImageList[position].videoView = videoItem
        videoItem?.run {
            imgContentVideoPreview.setImageUrl(imageList[position])
            bindVideo(feedMedia)

        }
        return (videoItem)
    }

    fun playVideo(mediaModel: MediaModel, position: Int = feedContentCarousel.activeIndex) {
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
                pvContentVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                pvContentVideo.player = videoPlayer?.getExoPlayer()
                pvContentVideo.videoSurfaceView?.setOnClickListener {
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
                cvProductTaggingParent.setOnClickListener {
                    openBottomSheet(createPostModel.completeImageList[index].products, MediaType.VIDEO)
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
        ivPlayContent.gone()
    }

    private fun showVideoLoading() {
        ivPlayContent.visible()
    }

    private fun toggleVolume(isMute: Boolean) {
        videoPlayer?.toggleVideoVolume(isMute)
    }

    private fun openBottomSheet(productList: List<RelatedProductItem>, mediaType: String) {
        createPostAnalytics.eventOpenProductTagBottomSheet(mediaType)
        val contentProductTagBS = ContentCreationProductTagBottomSheet()
        contentProductTagBS.show(Bundle.EMPTY,
            childFragmentManager,
            productList,
            this,
            mediaType = mediaType)

    }

    fun deleteAllProducts() {
        createPostModel.completeImageList.forEach {
            it.products.clear()
            it.tags.clear()

            if (it.type == MediaType.VIDEO) cvProductTaggingParent.hide()
            else {
                it.imageView?.run {
                    val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
                    val lihatProductTagView = findViewById<CardView>(R.id.product_tagging_button_parent)

                    lihatProductTagView.hide()
                    layout.removeAllViews()
                }
            }
        }

        updateTotalProductTaggedText()
        enableProductIcon()
        updateResultIntent()
    }

    override fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String
    ) {

        val currentImagePos = createPostModel.currentCorouselIndex
        val position = findProductIndexByProductId(productId,currentImagePos)

        if(isDeletedFromBubble)
            createPostAnalytics.eventDeleteProductTagPost(mediaType, productId)
        else
            createPostAnalytics.eventDeleteProductTagBottomSheet(mediaType, productId)

        removeExtraTagListElement(createPostModel.completeImageList[currentImagePos])
        try {
            createPostModel.completeImageList[currentImagePos].products.removeAt(position)
            createPostModel.completeImageList[currentImagePos].tags.removeAt(position)
        } catch (e: Exception) {
            Timber.e(e)
        }

        updateTotalProductTaggedText()

        //update tagIndex
        createPostModel.completeImageList[currentImagePos].tags.forEachIndexed { index, feedXMediaTagging ->
            feedXMediaTagging.tagIndex = index
        }

        val mediaModel = createPostModel.completeImageList[currentImagePos]
        if (getLatestTotalProductCount() < MAX_PRODUCT_TAG)
            enableProductIcon()
        if (mediaModel.type == MediaType.VIDEO)
            bindVideo(mediaModel)
        else
            removeProductTagViewFromImage(mediaModel, productId)

        updateResultIntent()

        if (createPostModel.completeImageList[currentImagePos].products.size == 0 && !isDeletedFromBubble)
            Toaster.build(requireView(),
                getString(com.tokopedia.content.common.R.string.feed_content_delete_toaster_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL).show()
    }

    private fun updateTotalProductTaggedText() {
        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"
        tvImagePosition.text = String.format(
            requireContext().getString(com.tokopedia.content.common.R.string.feed_content_position_text),
            pos
        )
    }

    private fun findProductIndexByProductId(productId: String, currentImagePos: Int): Int {
        createPostModel.completeImageList[currentImagePos].products.forEachIndexed { index, product ->
            if (product.id == productId)
                return index
        }
        return -1

    }

    override fun setContentAccountList(contentAccountList: List<ContentAccountUiModel>) {

    }

    override fun openProductTaggingPageOnPreviewMediaClick(position: Int) {

    }

    override fun clickProductTagBubbleAnalytics(mediaType: String, productId: String) {
        createPostAnalytics.eventClickProductTagBubble(mediaType, productId)
    }

    override fun updateTaggingInfoInViewModel(
        feedXMediaTagging: FeedXMediaTagging,
    ) {
        val tags = createPostModel.completeImageList[createPostModel.currentCorouselIndex].tags
        if (tags.size > feedXMediaTagging.tagIndex) {
            createPostModel.completeImageList[feedXMediaTagging.mediaIndex].tags[feedXMediaTagging.tagIndex] =
                feedXMediaTagging
            updateResultIntent()
        }
    }

    override fun clickContinueOnTaggingPage() {

    }

    override fun postFeed() {
        TODO("Not yet implemented")
    }

    private fun openProductTaggingScreen() {
        goToAttachProduct()
    }

    private fun goToAttachProduct() {
        activity?.let {
            val intent = RouteManager.getIntent(context,
                "tokopedia://productpickerfromshop?shopid=${userSession.shopId}&source=shop_product")
            intent.putExtra(PARAM_SHOP_NAME, createPostModel.shopName)
            intent.putExtra(PARAM_SHOP_BADGE, createPostModel.shopBadge)
            intent.putExtra(PARAM_PRODUCT_TAG_SOURCE, createPostModel.productTagSources.joinToString(separator = ","))
            intent.putExtra(PARAM_AUTHOR_ID, (requireActivity() as CreatePostActivityNew).selectedContentAccount.id)
            intent.putExtra(PARAM_AUTHOR_TYPE, (requireActivity() as CreatePostActivityNew).selectedContentAccount.type)
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
        val relatedProductItem = mapResultToRelatedProductItem(data)
        if(!isProductAlreadyAddedOnImage(relatedProductItem)) {
            createPostModel.productIdList.add(relatedProductItem.id)
            createPostModel.completeImageList[createPostModel.currentCorouselIndex].products.add(
                relatedProductItem)
        }

        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]
        removeExtraTagListElement(mediaModel)

        updateTotalProductTaggedText()

        if (getLatestTotalProductCount() == MAX_PRODUCT_TAG)
            disableProductIcon()

        if (mediaModel.products.size > 0) {
            if (mediaModel.type == MediaType.VIDEO)
                bindVideo(mediaModel)
            else
                bindImage(mediaModel, createPostModel.currentCorouselIndex)
        }
        updateResultIntent()
    }

    private fun mapResultToRelatedProductItem(data: Intent?): RelatedProductItem {
        return RelatedProductItem(
            id = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_ID) ?: "",
            name = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_NAME) ?: "",
            price = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_PRICE) ?: "",
            image = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_IMAGE) ?: "",
            priceOriginalFmt = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_PRICE_ORIGINAL_FMT) ?: "",
            priceDiscountFmt = data?.getStringExtra(ProductTagActivity.RESULT_PRODUCT_PRICE_DISCOUNT_FMT) ?: "",
            isDiscount = data?.getBooleanExtra(ProductTagActivity.RESULT_PRODUCT_IS_DISCOUNT, false) ?: false,
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
        val color = context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN300 ) }
        color?.let {
            icProductTag.setColorFilter(it)
            tvContentTagProduct.setTextColor(it)
        }

    }
    private fun enableProductIcon(){
        val color = context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN900 ) }
        color?.let {
            icProductTag.setColorFilter(it)
            tvContentTagProduct.setTextColor(it)
        }
    }

    private fun removeProductTagViewFromImage(media: MediaModel, productId: String) {
        val products = media.products
        val imageItem = media.imageView

        imageItem?.run {
            val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
            val lihatProductTagView = findViewById<CardView>(R.id.product_tagging_button_parent)

            lihatProductTagView.showWithCondition(products.isNotEmpty())

            for (view in layout.children) {
                if (view.tag == productId) layout.removeView(view)
            }
        }
    }

    private fun bindImage(media: MediaModel, mediaIndex: Int) {
        val products = media.products
        val imageItem = media.imageView
        val listener = this

        removeExtraTagListElement(media)

        imageItem?.run {
            val lihatProductTagView = findViewById<CardView>(R.id.product_tagging_button_parent)
            val postImage = findViewById<ImageUnify>(R.id.content_creation_post_image)

            lihatProductTagView.showWithCondition(products.isNotEmpty())

            lihatProductTagView.setOnClickListener {
                openBottomSheet(createPostModel.completeImageList[createPostModel.currentCorouselIndex].products, MediaType.IMAGE)
            }
            val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
            layout.removeAllViews()

                media.tags.forEachIndexed { index, feedXMediaTagging ->
                    val tagViewProvider = TagViewProvider()
                    val view = tagViewProvider.getTagView(context,
                        products,
                        index,
                        listener,
                        feedXMediaTagging,
                        layout)
                    if (view != null) {
                        try {
                            Handler().postDelayed(Runnable {
                                val bitmap = postImage?.drawable?.toBitmap()
                                tagViewProvider.addViewToParent(view,
                                    layout,
                                    feedXMediaTagging,
                                    bitmap)
                            }, DEFAULT_DELAY)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                }


        }
    }

    private fun bindVideo(mediaModel: MediaModel) {
        mediaModel.videoView?.run {
            cvProductTaggingParent.showWithCondition(mediaModel.products.isNotEmpty())
            cvProductTaggingParent.setOnClickListener {
                openBottomSheet(createPostModel.completeImageList[createPostModel.currentCorouselIndex].products, MediaType.VIDEO)
            }
        }
    }

    private fun isProductAlreadyAddedOnImage(product: RelatedProductItem):Boolean {
        val currentImagePos = createPostModel.currentCorouselIndex
        val tagsAlreadyAdded = createPostModel.completeImageList[currentImagePos].tags
        val productsAdded = createPostModel.completeImageList[currentImagePos].products
        var isAdded = false
        val tagToBeAdded = tagsAlreadyAdded.lastOrNull()
        tagToBeAdded?.let {
            if (productsAdded.size > 0) {
                productsAdded.forEachIndexed { index, relatedProductItem ->
                    if (relatedProductItem.id == product.id) {
                        isAdded = true
                        tagsAlreadyAdded[index] = tagToBeAdded
                        tagsAlreadyAdded[index].tagIndex = index
                    }
                }
            }
        }
        if (isAdded) {
            createPostModel.completeImageList[createPostModel.currentCorouselIndex].tags = tagsAlreadyAdded
            removeExtraTagListElement(createPostModel.completeImageList[createPostModel.currentCorouselIndex])
        }
        return isAdded
    }
    fun detach(
    ) {
        if (videoPlayer != null) {
            videoPlayer?.pause()
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.destroy()
            videoPlayer = null
        }
    }
    private fun calculateGreyAreaX(parent: ConstraintLayout, bitmap: Bitmap): Int {
        return if (bitmap.width > bitmap.height) {
            val newBitmapHeight = (parent.height * bitmap.height) / bitmap.width
            (parent.height - newBitmapHeight) / 2
        } else
            0
    }

    private fun calculateGreyAreaY(parent: ConstraintLayout, bitmap: Bitmap): Int {
        return if (bitmap.height > bitmap.width) {
            val newBitmapHeight = (parent.width * bitmap.width) / bitmap.height
            (parent.width - newBitmapHeight) / 2
        } else
            0
    }
    private fun setMediaWidthAndHeight(){
        val mediaModel = createPostModel.completeImageList[0]
        try {

                var mBmp = Glide.with(requireActivity())
                        .asBitmap()
                        .load(mediaModel.path)
                        .submit()
                        .get()
                createPostModel.mediaWidth = mBmp.width.toPx()
                createPostModel.mediaHeight = mBmp.height.toPx()

        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}

