package com.tokopedia.createpost.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.createpost.view.bottomSheet.ContentCreationProductTagBottomSheet
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.createpost.view.posttag.TagViewProvider
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.imagepicker_insta.common.ui.menu.MenuManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
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

class CreatePostPreviewFragmentNew : BaseCreatePostFragmentNew(), CreateContentPostCommonListener {

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
        private const val PARAM_PRODUCT = "product"
        private const val PARAM_SHOP_NAME = "shop_name"

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
        initView()
    }

    private fun initVar() {
        createPostModel = arguments?.getParcelable(CreatePostViewModel.TAG) ?: CreatePostViewModel()

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuTitle =  activity?.getString(R.string.feed_content_text_lanjut)
        if(!menuTitle.isNullOrEmpty()) {
            MenuManager.addCustomMenu(activity, menuTitle, true, menu) {
                activityListener?.clickContinueOnTaggingPage()
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MenuManager.MENU_ITEM_ID -> {
                activityListener?.clickContinueOnTaggingPage()
                return true
            }
        }
        return false
    }

    private fun initView() {
        val relatedProducts = ArrayList(createPostModel.relatedProducts)
        productAdapter.setList(relatedProducts)
        productAdapter.removeEmpty()
        createPostModel.maxProduct = 5
        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"
        image_position_text?.text = String.format(
            requireContext().getString(R.string.feed_content_position_text),
            pos
        )
        if (getLatestTotalProductCount() == 5)
            disableProductIcon()
        else
            enableProductIcon()

        product_tag_button.setOnClickListener {
            setProductTagListener()
        }
        content_tag_product_text.setOnClickListener {
            setProductTagListener()
        }

        updateCarouselView()
        feed_content_carousel?.activeIndex = createPostModel.currentCorouselIndex
    }
    private fun setProductTagListener(){
        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]

        createPostAnalytics.eventClickTagProductIcon(mediaModel.type)

        if (getLatestTotalProductCount() < 5) {
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
                getString(R.string.feed_content_more_than_5_product_tag),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR).show()
        }

    }

    private fun removeExtraTagListElement(mediaModel: MediaModel) {
        if (mediaModel.tags.size > mediaModel.products.size) {
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
                                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
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
                    if (createPostModel.completeImageList[createPostModel.currentCorouselIndex].type == MediaType.VIDEO)
                        createPostModel.completeImageList[createPostModel.currentCorouselIndex].isPlaying =
                            false
                    createPostModel.currentCorouselIndex = current
                    page_indicator.setCurrentIndicator(current)

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
                feed_content_layout_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
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
        feed_content_ic_play?.gone()
    }

    private fun showVideoLoading() {
        feed_content_ic_play?.visible()
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
        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"
        image_position_text.text = String.format(
            requireContext().getString(R.string.feed_content_position_text),
            pos
        )

        //update tagIndex
        createPostModel.completeImageList[currentImagePos].tags.forEachIndexed { index, feedXMediaTagging ->
            feedXMediaTagging.tagIndex = index
        }

        val mediaModel = createPostModel.completeImageList[currentImagePos]
        if (getLatestTotalProductCount() < 5)
            enableProductIcon()
        if (mediaModel.type == MediaType.VIDEO)
            bindVideo(mediaModel)
        else
            bindImageAfterDelete(mediaModel, currentImagePos, productId)

        updateResultIntent()

        if (createPostModel.completeImageList[currentImagePos].products.size == 0 && !isDeletedFromBubble)
            Toaster.build(requireView(),
                getString(R.string.feed_content_delete_toaster_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL).show()


    }
    private fun findProductIndexByProductId(productId: String, currentImagePos: Int): Int {
        createPostModel.completeImageList[currentImagePos].products.forEachIndexed { index, product ->
            if (product.id == productId)
                return index
        }
        return -1

    }

    override fun updateHeader(header: HeaderViewModel) {

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
            val relatedProductItem = mapResultToRelatedProductItem(it)
            if(!isProductAlreadyAddedOnImage(relatedProductItem)) {
                createPostModel.productIdList.add(it.pId!!)
                createPostModel.completeImageList[createPostModel.currentCorouselIndex].products.add(
                    relatedProductItem)
            }
        }
        val mediaModel = createPostModel.completeImageList[createPostModel.currentCorouselIndex]
        removeExtraTagListElement(mediaModel)

        val pos = "(${getLatestTotalProductCount()}/${createPostModel.maxProduct})"

        try {
            image_position_text?.text = String.format(
                    requireContext().getString(R.string.feed_content_position_text),
                    pos
            )
        } catch (e: Exception) {
            Timber.e(e)
        }

        if (getLatestTotalProductCount() == 5)
            disableProductIcon()

        if (mediaModel.products.size > 0) {
            if (mediaModel.type == MediaType.VIDEO)
                bindVideo(mediaModel)
            else
                bindImage(mediaModel, createPostModel.currentCorouselIndex)
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
            priceDiscountFmt = item.campaign?.dPriceFormatted!!,
            isDiscount = (item.campaign?.dPrice?.toInt()?:0)!=0
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
            product_tag_button.setColorFilter(it)
            content_tag_product_text.setTextColor(it)
        }

    }
    private fun enableProductIcon(){
        val color = context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN900 ) }
        color?.let {
            product_tag_button.setColorFilter(it)
            content_tag_product_text.setTextColor(it)
        }
    }
    private fun bindImageAfterDelete(media: MediaModel, mediaIndex: Int, productId: String) {
        val products = media.products
        val imageItem = media.imageView
        imageItem?.run {
            val layout = findViewById<ConstraintLayout>(R.id.product_tagging_parent_layout)
            val lihatProductTagView = findViewById<CardView>(R.id.product_tagging_button_parent)
            lihatProductTagView.showWithCondition(products.isNotEmpty())

            for (view in layout.children) {
                if (view.tag == productId)
                    layout.removeView(view)
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
                            }, 50)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                }


        }
    }

    private fun bindVideo(mediaModel: MediaModel) {
        mediaModel.videoView?.run {
            content_product_tagging_parent.showWithCondition(mediaModel.products.isNotEmpty())
            content_product_tagging_parent?.setOnClickListener {
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
}

