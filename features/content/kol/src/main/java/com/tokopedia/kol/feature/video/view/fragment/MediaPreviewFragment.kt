package com.tokopedia.kol.feature.video.view.fragment

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.view.adapter.MediaPagerAdapter
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.kol.feature.video.view.adapter.MediaTagAdapter
import com.tokopedia.kol.feature.video.view.viewmodel.FeedMediaPreviewViewModel
import com.tokopedia.kolcommon.util.TimeConverter
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_media_preview.*
import javax.inject.Inject

class MediaPreviewFragment: BaseDaggerFragment() {
    var selectedIndex = 0

    var buttonTagAction: UnifyButton? = null

    var postAuthor = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var feedAnalyticTracker: FeedAnalyticTracker

    private lateinit var mediaPreviewViewModel: FeedMediaPreviewViewModel
    private val tagsAdapter by lazy { MediaTagAdapter(mutableListOf(), {
        mediaPreviewViewModel.isMyShop(it)
    }, this::toggleWishlist){
        postTagItem, isMyShop ->
            feedAnalyticTracker.eventMediaDetailClickBuy(
                    postAuthor,
                    postTagItem.id,
                    postTagItem.text,
                    postTagItem.price,
                    1,
                    postTagItem.shop[0].shopId.toIntOrZero(),
                    "")
            if (isMyShop) onGoToLink(postTagItem.applink)
            else checkAddToCart(postTagItem)
    } }
    private val tagsBottomSheet: CloseableBottomSheetDialog? by lazy {
        context?.let {
            val closeBottomSheet = CloseableBottomSheetDialog.createInstance(it)
            val childView = LayoutInflater.from(it).inflate(R.layout.bottomsheet_content_tag_list, null)
            val tagListView = childView.findViewById<VerticalRecyclerView>(R.id.recycler_view)
            tagListView.adapter = tagsAdapter
            closeBottomSheet.setCustomContentView(childView, getString(R.string.kol_lets_shop), true)
            closeBottomSheet
        }
    }

    override fun getScreenName(): String? = FeedAnalyticTracker.Screen.MEDIA_PREVIEW

    override fun initInjector() {
        getComponent(KolComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaPreviewViewModel.postDetailLive.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetDetail(it.data)
                is Fail -> onErrorGetDetail(it.throwable)
            }
        })

        mediaPreviewViewModel.postFooterLive.observe(this, Observer {
            val (footer, footerTemplate) = it ?: return@Observer
            bindFooter(footer, footerTemplate)
        })

        mediaPreviewViewModel.postTagLive.observe(this, Observer { postTag ->
            postTag?.let { bindTags(it) }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mediaPreviewViewModel = ViewModelProviders.of(this, viewModelFactory)[FeedMediaPreviewViewModel::class.java]
            mediaPreviewViewModel.postId = arguments?.getString(ARG_POST_ID, "0") ?: "0"
            selectedIndex = arguments?.getInt(ARG_MEDIA_INDEX, 0) ?: 0
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonTagAction = view.findViewById(R.id.button_tag_action)

        action_back.setOnClickListener { activity?.onBackPressed() }
        mediaPreviewViewModel.getPostDetail()
        feedAnalyticTracker.eventOpenMediaPreview()
    }

    override fun onDestroy() {
        mediaPreviewViewModel.postDetailLive.removeObservers(this)
        mediaPreviewViewModel.postFooterLive.removeObservers(this)
        mediaPreviewViewModel.postTagLive.removeObservers(this)
        mediaPreviewViewModel.flush()
        super.onDestroy()
    }

    private fun onSuccessGetDetail(data: PostDetailViewModel) {
        val dynamicPost = data.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?
        dynamicPost?.let {
            bindToolbar(it)
            val mediaGrid = it.contentList.filterIsInstance<MultimediaGridViewModel>().firstOrNull()
            if (mediaGrid != null){
                bindMedia(mediaGrid.mediaItemList)
            }
        }
    }

    private fun bindMedia(mediaItems: List<MediaItem>) {
        val adapter = MediaPagerAdapter(mediaItems.toMutableList(), childFragmentManager)
        pager_indicator.text = getString(R.string.af_indicator_media, selectedIndex+1, adapter.count)
        media_pager.adapter = adapter
        updateDirectionMedia(0, adapter.count)
        media_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var lastPosition = 0
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                pager_indicator.text = getString(R.string.af_indicator_media, position+1, adapter.count)
                (adapter.getRegisteredFragment(lastPosition) as? MediaHolderFragment)?.fragmentHiding()
                (adapter.getRegisteredFragment(position) as? MediaHolderFragment)?.fragmentShowing()
                lastPosition = position
                updateDirectionMedia(position, adapter.count)
            }

        })
        media_pager.currentItem = selectedIndex

        action_prev.setOnClickListener {
            val currentIndex = media_pager.currentItem
            if (currentIndex > 0){
                media_pager.currentItem = currentIndex - 1
            }
        }

        action_next.setOnClickListener {
            val currentIndex = media_pager.currentItem
            if (currentIndex < adapter.count - 1){
                media_pager.currentItem = currentIndex + 1
            }
        }
    }

    private fun updateDirectionMedia(pos: Int, count: Int, forceToHide: Boolean = false) {
        action_prev.showWithCondition(!forceToHide && pos > 0 && count > 1)
        action_next.showWithCondition(!forceToHide && pos < count-1 && count > 1)
    }

    private fun bindTags(tags: PostTag) {

        when {
            tags.totalItems > 1 -> {
                tag_count.text = getString(R.string.kol_total_post_tag, tags.totalItems)
                tag_count.visible()
                tag_picture.gone()
                val minRate = tags.items.filter { it.rating > 0 }.map { it.rating }.min() ?: 0

                tag_rating.shouldShowWithAction(minRate > 0){
                    tag_rating.rating = minRate.toFloat()
                }

                val minPrice = tags.items.map { CurrencyFormatHelper.convertRupiahToInt(it.price) }.min() ?: 0
                tag_title.text = getString(R.string.kol_template_start_price,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true))

                buttonTagAction?.text = getString(R.string.kol_see_product)
                buttonTagAction?.buttonType = UnifyButton.Type.MAIN
                tagsAdapter.updateTags(tags.items)
                buttonTagAction?.setOnClickListener {
                    feedAnalyticTracker.eventMediaDetailClickLihat(mediaPreviewViewModel.postId)
                    tagsBottomSheet?.show()
                }

                action_favorite.gone()
                buttonTagAction?.visible()
            }
            tags.totalItems == 1 -> {
                val tagItem = tags.items[0]
                tag_count.gone()
                tag_rating.shouldShowWithAction(tagItem.rating > 0){
                    tag_rating.rating = tagItem.rating.toFloat()
                }

                tag_title.text = tagItem.price
                val shop = tagItem.shop.firstOrNull()
                val ctaBtn = tagItem.buttonCTA.firstOrNull()
                if (shop == null || mediaPreviewViewModel.isMyShop(shop.shopId)){
                    buttonTagAction?.text = getString(R.string.kol_see_product)
                    action_favorite.gone()
                    buttonTagAction?.buttonType = UnifyButton.Type.MAIN
                    buttonTagAction?.setOnClickListener { onGoToLink(tagItem.applink) }
                } else {
                    buttonTagAction?.buttonType = UnifyButton.Type.TRANSACTION
                    if (ctaBtn == null || ctaBtn.text.isBlank()){
                        buttonTagAction?.isEnabled = false
                        buttonTagAction?.text = getString(com.tokopedia.feedcomponent.R.string.empty_product)
                    } else {
                        buttonTagAction?.isEnabled = true
                        buttonTagAction?.text = getString(com.tokopedia.feedcomponent.R.string.string_posttag_buy)
                    }
                    buttonTagAction?.setOnClickListener {
                        feedAnalyticTracker.eventMediaDetailClickBuy(
                                postAuthor,
                                tagItem.id,
                                tagItem.text,
                                tagItem.price,
                                1,
                                tagItem.shop[0].shopId.toIntOrZero(),
                                "")
                        checkAddToCart(tagItem)
                    }
                    action_favorite.visible()
                    context?.let {
                        action_favorite.setImageDrawable(ContextCompat.getDrawable(it,
                                if (tags.items[0].isWishlisted) com.tokopedia.design.R.drawable.ic_wishlist_checked
                                else com.tokopedia.design.R.drawable.ic_wishlist_unchecked))
                    }
                    action_favorite.setOnClickListener {
                        val product = tags.items[0]
                        toggleWishlist(!product.isWishlisted, product.id, 0)
                    }
                }

                tag_picture.loadImageRounded(tags.items[0].thumbnail, resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8))
                tag_picture.visible()
                buttonTagAction?.visible()
            }
            else -> overlay_tags.gone()
        }
    }

    private fun checkAddToCart(tagItem: PostTagItem) {
        mediaPreviewViewModel.addToCart(tagItem,
                { context?.let { RouteManager.route(context, ApplinkConstInternalMarketplace.CART) }}){
                _ , postTagItem -> onGoToLink(postTagItem.applink)
        }
    }

    private fun onGoToLink(link: String) {
        if (activity != null && !TextUtils.isEmpty(link)) {
            if (RouteManager.isSupportApplink(activity, link)) {
                RouteManager.route(activity, link)
            } else {
                RouteManager.route(
                        activity,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                )
            }
        }
    }

    private fun toggleWishlist(isWishListAction: Boolean, productId: String, pos: Int){
        if (mediaPreviewViewModel.isSessionActive){
            mediaPreviewViewModel.toggleWishlist(isWishListAction, productId, pos, this::onErrorToggleWishlist)
        } else {
            context?.let { startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQ_CODE_LOGIN) }
        }
    }

    private fun onErrorToggleWishlist(message: String) {
        showToastError(message)
    }

    private fun bindToolbar(dynamicPost: DynamicPostViewModel) {
        val templateHeader = dynamicPost.template.cardpost.header
        val header = dynamicPost.header
        postAuthor = header.followCta.authorType
        authorImage.shouldShowWithAction(templateHeader.avatar && header.avatar.isNotBlank()){
            authorImage.loadImageCircle(header.avatar)
            authorImage.setOnClickListener{onHeaderClicked(header)}
        }
        authorBadge.shouldShowWithAction(templateHeader.avatarBadge && header.avatarBadgeImage.isNotBlank()){
            authorBadge.loadImage(header.avatarBadgeImage, com.tokopedia.design.R.drawable.error_drawable)
        }
        authorTitle.shouldShowWithAction(templateHeader.avatarTitle && header.avatarTitle.isNotBlank()){
            authorTitle.text = header.avatarTitle
            authorTitle.setOnClickListener{onHeaderClicked(header)}
        }
        authorSubtitile.shouldShowWithAction(templateHeader.avatarDate && header.avatarDate.isNotBlank()){
            authorSubtitile.text = TimeConverter.generateTime(context, header.avatarDate)
        }
    }

    private fun onHeaderClicked(header: Header) {
        feedAnalyticTracker.eventMediaDetailClickAvatar(mediaPreviewViewModel.postId)
        feedAnalyticTracker.eventClickMediaPreviewAvatar(mediaPreviewViewModel.postId, header.followCta.authorType)
        RouteManager.route(activity, header.avatarApplink)
    }

    private fun bindFooter(footer: PostDetailFooterModel, template: TemplateFooter?) {
        groupLike.shouldShowWithAction(template?.like == true){
            label_like.text = if (footer.totalLike > 0) footer.totalLike.toString()
                else getString(com.tokopedia.feedcomponent.R.string.kol_action_like)
            val color = context?.let { ContextCompat.getColor(it,
                    if (footer.isLiked) R.color.kol_green_g500 else com.tokopedia.unifyprinciples.R.color.Unify_N0 ) }
            color?.let {
                icon_thumb.setColorFilter(it, PorterDuff.Mode.MULTIPLY)
                label_like.setTextColor(it)
            }

        }

        icon_thumb.setOnClickListener { doLikePost(!footer.isLiked) }
        label_like.setOnClickListener { doLikePost(!footer.isLiked) }

        groupComment.shouldShowWithAction(template?.comment == true){
            label_comment.text = if (footer.totalComment > 0) footer.totalComment.toString()
                else getString(com.tokopedia.feedcomponent.R.string.kol_action_comment)
        }

        icon_comment.setOnClickListener { doComment() }
        label_comment.setOnClickListener { doComment() }

        groupShare.showWithCondition(template?.share == true)

        icon_share.setOnClickListener { doShare(String.format("%s %s", footer.shareData.description, footer.shareData.url)
                , footer.shareData.title) }
        label_share.setOnClickListener {
            doShare(String.format("%s %s", footer.shareData.description, footer.shareData.url)
                    , footer.shareData.title)
        }
    }

    private fun doLikePost(isLikeAction: Boolean) {
        if (mediaPreviewViewModel.isSessionActive) {
            mediaPreviewViewModel.doLikePost(isLikeAction, this::onErrorLikePost)
        } else {
            activity?.let {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQ_CODE_LOGIN)
            }
        }
    }

    private fun doComment() {
        activity?.let {
            val (intent, reqCode) = if (mediaPreviewViewModel.isSessionActive)
                KolCommentActivity.getCallingIntent(it, mediaPreviewViewModel.postId.toInt(), 0) to REQ_CODE_COMMENT
            else RouteManager.getIntent(it, ApplinkConst.LOGIN) to REQ_CODE_LOGIN

            startActivityForResult(intent, reqCode)
        }
    }

    private fun doShare(body: String, title: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(
                Intent.createChooser(sharingIntent, title)
        )
    }

    private fun onErrorLikePost(t: Throwable) {
        context?.let { showToastError(ErrorHandler.getErrorMessage(it, t)) }
    }

    private fun onErrorGetDetail(throwable: Throwable) {
        context?.let { showToastError(ErrorHandler.getErrorMessage(it, throwable)) }
    }

    private fun showToastError(message: String){
        view?.let { v ->  Toaster.showErrorWithAction(v, message,
                Snackbar.LENGTH_LONG, getString(com.tokopedia.abstraction.R.string.title_ok), View.OnClickListener {  })}
    }

    fun showDetail(visible: Boolean) {
        media_pager.adapter?.let {
            updateDirectionMedia(media_pager.currentItem, it.count, !visible)
        }
        if (visible){
            overlay_tags.visible()
            overlay_toolbar.visible()
        } else {
            overlay_tags.gone()
            overlay_toolbar.gone()
        }
    }

    companion object{
        const val ARG_POST_ID = "post_id"
        const val ARG_MEDIA_INDEX = "media_index"
        private const val REQ_CODE_LOGIN = 0x01
        private const val REQ_CODE_COMMENT = 0x02

        @JvmStatic
        fun createInstance(postId: String, index: Int): Fragment = MediaPreviewFragment().apply {
            arguments = Bundle().also {
                it.putString(ARG_POST_ID, postId)
                it.putInt(ARG_MEDIA_INDEX, index)
            }
        }
    }
}