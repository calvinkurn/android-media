package com.tokopedia.navigation_common.ui

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholderAndError
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.navigation_common.databinding.ItemBottomNavbarBinding
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Key
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Type
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Variant
import com.tokopedia.navigation_common.util.LottieCacheManager
import com.tokopedia.navigation_common.util.inDarkMode
import com.tokopedia.navigation_common.util.isDeviceAnimationDisabled
import com.tokopedia.unifyprinciples.NestShadow.isDarkMode
import com.tokopedia.navigation_common.R as navigation_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class BottomNavBarView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mListener: Listener? = null

    private val cacheManager = LottieCacheManager(context)

    private var mIsForceDarkMode = false
    private val isDarkMode: Boolean
        get() = if (mIsForceDarkMode) true else context.isDarkMode()

    private val darkModeContext: Context = context.inDarkMode

    private val uiModeAwareContext
        get() = if (isDarkMode) darkModeContext else context

    private var mSelectedItemId: BottomNavItemId? = null

    private val modelMap = mutableMapOf<BottomNavItemId, BottomNavBarUiModel>()

    private val jumperStateMap = mutableMapOf<BottomNavItemId, Boolean>()

    init {
        super.setOrientation(HORIZONTAL)
        refresh()
    }

    override fun setOrientation(orientation: Int) {
        error("Orientation is not allowed to be changed")
    }

    fun setJumperForId(id: BottomNavItemId, shouldChangeToJumper: Boolean) {
        val oldState = jumperStateMap[id]
        jumperStateMap[id] = shouldChangeToJumper
        if (oldState != shouldChangeToJumper) refresh()
    }

    fun setModelList(modelList: List<BottomNavBarUiModel>) {
        modelMap.clear()
        modelMap.putAll(modelList.associateBy { it.uniqueId })
        buildMenu(modelList)

        modelList.forEach {
            it.assets.values.forEach loadAsset@{ asset ->
                if (asset !is Type.LottieUrl) return@loadAsset
                asset.preload()
            }
        }

        val firstModel = modelList.firstOrNull() ?: return
        select(mSelectedItemId ?: firstModel.uniqueId)
    }

    fun select(itemId: BottomNavItemId, forceSelect: Boolean = false) {
        val model = modelMap[itemId]

        if (model != null) {
            val isSelected = mListener?.onItemSelected(this, model, mSelectedItemId == itemId, model.isJumper())
            if (isSelected != true && !forceSelect) return
        } else {
            if (!forceSelect) return
        }

        mSelectedItemId = itemId
        rebindAllItems(itemId)
    }

    fun forceDarkMode(shouldDarkMode: Boolean) {
        mIsForceDarkMode = shouldDarkMode
        refresh()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun findBottomNavItemViewById(id: BottomNavItemId): View? {
        return children.firstOrNull { it.stateHolder?.model?.uniqueId == id }
    }

    private fun refresh() {
        bindBackground()
        rebindAllItems()
    }

    private fun rebindAllItems(selectedItemId: BottomNavItemId? = mSelectedItemId) {
        children.forEach {
            val stateHolder = it.stateHolder ?: return@forEach
            val binding = runCatching { ItemBottomNavbarBinding.bind(it) }.getOrNull() ?: return@forEach
            binding.bindModel(stateHolder, selectedItemId == stateHolder.model.uniqueId)
        }
    }

    private fun buildMenu(modelList: List<BottomNavBarUiModel>) {
        removeAllViews()
        modelList.map(::buildMenuView).forEach(::addToParent)
    }

    private fun buildMenuView(model: BottomNavBarUiModel): View {
        val binding = ItemBottomNavbarBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

        val stateHolder = StateHolder.init(model)
        binding.root.tag = stateHolder
        binding.bindModel(stateHolder, null)

        return binding.root
    }

    private fun addToParent(view: View) {
        val lParams = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT
        )
        lParams.weight = 1f
        view.layoutParams = lParams
        addView(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun ItemBottomNavbarBinding.bindModel(
        stateHolder: StateHolder,
        isSelected: Boolean?
    ) {
        val (model, prevIsSelected, prevShouldUseJumper) = stateHolder
        val shouldUseJumper = model.jumper != null && model.isJumper()

        ivIcon.bindAsset(model, isSelected, prevIsSelected, shouldUseJumper, prevShouldUseJumper)
        tvTitle.bindText(model, isSelected, shouldUseJumper)

        rippleView.background = ContextCompat.getDrawable(uiModeAwareContext, navigation_commonR.drawable.bg_ripple_container)

        root.setOnTouchListener(BottomNavBarItemRippleTouchListener(root, rippleView))
        root.setOnClickListener { select(model.uniqueId) }

        updateState(stateHolder.copy(isSelected = isSelected, isJumper = shouldUseJumper))
    }

    private fun ItemBottomNavbarBinding.updateState(newState: StateHolder) {
        root.tag = newState
    }

    private val View.stateHolder: StateHolder?
        get() = tag as? StateHolder

    private fun LottieAnimationView.bindAsset(
        model: BottomNavBarUiModel,
        isSelected: Boolean?,
        prevIsSelected: Boolean?,
        shouldUseJumper: Boolean,
        prevIsJumper: Boolean
    ) {
        val assetPlaylist = if (shouldUseJumper) {
            getJumperAssets(model, isSelected, prevIsSelected, prevIsJumper)
        } else {
            getNormalAssets(model, isSelected, prevIsSelected, prevIsJumper)
        } ?: return

        bindAssetPlaylist(assetPlaylist)
    }

    private fun LottieAnimationView.bindAssetPlaylist(
        playlist: AssetPlaylist
    ) {
        removeAllAnimatorListeners()
        pauseAnimation()
        clearImage()

        repeatCount = 0

        when (playlist) {
            is ImagePlaylist -> bindImagePlaylist(playlist)
            is LottiePlaylist -> bindLottiePlaylist(playlist)
        }
    }

    private fun LottieAnimationView.bindImagePlaylist(playlist: ImagePlaylist) {
        when (val image = playlist.image) {
            is Type.ImageUrl -> {
                loadImageWithoutPlaceholderAndError(image.url) {
                    setCacheStrategy(MediaCacheStrategy.DATA)
                }
            }
            is Type.ImageRes -> {
                setImageDrawable(
                    ContextCompat.getDrawable(uiModeAwareContext, image.res)
                )
            }
        }
    }

    private fun LottieAnimationView.bindLottiePlaylist(playlist: LottiePlaylist) {
        playlist.firstAsset.toLottieTask()
            .addListener {
                repeatCount = if (playlist.nextAsset == null && playlist.isInfinite) {
                    LottieDrawable.INFINITE
                } else {
                    0
                }
                setComposition(it)
                playAnimation()
            }

        addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                if (playlist.nextAsset == null) return
                playlist.nextAsset.toLottieTask()
                    .addListener {
                        repeatCount = if (playlist.isInfinite) LottieDrawable.INFINITE else 0
                        setComposition(it)
                        playAnimation()
                    }
            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    private fun Type.Lottie.toLottieTask(): LottieTask<LottieComposition> {
        return when (this) {
            is Type.LottieUrl -> LottieCompositionFactory.fromUrl(context, url)
            is Type.LottieRes -> LottieCompositionFactory.fromRawRes(context, res)
        }
    }

    // TODO("Revisit this logic")
    private fun getNormalAssets(
        model: BottomNavBarUiModel,
        isSelected: Boolean?,
        prevIsSelected: Boolean?,
        prevIsJumper: Boolean
    ): AssetPlaylist? {
        val isDarkMode = this@BottomNavBarView.isDarkMode
        val variant = if (isDarkMode) Variant.Dark else Variant.Light
        return when {
            isSelected == null -> {
                ImagePlaylist.of(model.assets[Key.ImageInactive + variant])
            }
            !isSelected && prevIsSelected != true -> {
                ImagePlaylist.of(model.assets[Key.ImageInactive + variant])
            }
            (context.isDeviceAnimationDisabled() || isSelected == prevIsSelected) && !prevIsJumper -> {
                val key = if (isSelected) Key.ImageActive else Key.ImageInactive
                ImagePlaylist.of(model.assets[key + variant])
            }
            else -> {
                run {
                    val asset = if (prevIsJumper && model.jumper != null) {
                        if (isSelected) {
                            LottiePlaylist.of(model.jumper.assets[Key.AnimInactive + variant])
                        } else {
                            ImagePlaylist.of(model.assets[Key.ImageInactive + variant])
                        }
                    } else {
                        val key = if (isSelected) Key.AnimActive else Key.AnimInactive
                        LottiePlaylist.of(model.assets[key + variant])
                    }

                    val (firstAssetCached, secondAssetCached) = when (asset) {
                        is LottiePlaylist -> {
                            asset.firstAsset.isCached to (asset.nextAsset?.isCached ?: false)
                        }
                        else -> false to false
                    }

                    if (firstAssetCached) {
                        if (secondAssetCached) {
                            asset
                        } else {
                            val lottiePlaylist = asset as LottiePlaylist
                            LottiePlaylist.of(lottiePlaylist.firstAsset, isInfinite = lottiePlaylist.isInfinite)
                        }
                    } else {
                        asset?.let { playlist ->
                            if (playlist is LottiePlaylist) {
                                playlist.firstAsset.preload()
                                playlist.nextAsset?.preload()
                            }
                        }

                        ImagePlaylist.of(
                            model.assets[(if (isSelected) Key.ImageActive else Key.ImageInactive) + variant]
                        )
                    }
                }
            }
        }
    }

    private fun getJumperAssets(
        model: BottomNavBarUiModel,
        isSelected: Boolean?,
        prevIsSelected: Boolean?,
        prevIsJumper: Boolean
    ): AssetPlaylist? {
        val assets = model.jumper?.assets ?: return null
        val isDarkMode = this@BottomNavBarView.isDarkMode
        val variant = if (isDarkMode) Variant.Dark else Variant.Light
        return when {
            isSelected != true -> {
                ImagePlaylist.of(assets[Key.ImageInactive + variant])
            }
            context.isDeviceAnimationDisabled() -> {
                LottiePlaylist.of(assets[Key.AnimActive + variant])
            }
            isSelected == prevIsSelected -> {
                if (prevIsJumper) {
                    null
                } else {
                    LottiePlaylist.of(
                        assets[Key.AnimActive + variant],
                        assets[Key.AnimIdle + variant],
                        isInfinite = true
                    )
                }
            }
            else -> {
                LottiePlaylist.of(assets[Key.AnimIdle + variant], isInfinite = true)
            }
        }
    }

    private fun TextView.bindText(model: BottomNavBarUiModel, isSelected: Boolean?, shouldUseJumper: Boolean) {
        text = if (!shouldUseJumper || model.jumper == null || isSelected != true) {
            model.title
        } else {
            model.jumper.title
        }
        setTextColor(
            ContextCompat.getColor(
                uiModeAwareContext,
                if (isSelected == true) {
                    unifyprinciplesR.color.Unify_GN500
                } else {
                    navigation_commonR.color.dynamic_bottom_navbar_item_title
                }
            )
        )
    }

    private fun bindBackground() {
        setBackgroundColor(
            ContextCompat.getColor(uiModeAwareContext, unifyprinciplesR.color.Unify_NN0)
        )
    }

    private fun BottomNavBarUiModel.isJumper(): Boolean {
        return jumperStateMap[uniqueId] == true
    }

    private val Type.Lottie.isCached: Boolean
        get() {
            return when (this) {
                is Type.LottieUrl -> cacheManager.isUrlLoaded(url)
                is Type.LottieRes -> true
            }
        }

    private fun Type.Lottie.preload() {
        when (this) {
            is Type.LottieUrl -> cacheManager.preloadFromUrl(url)
            is Type.LottieRes -> {}
        }
    }

    interface Listener {
        fun onItemSelected(
            view: BottomNavBarView,
            model: BottomNavBarUiModel,
            isReselected: Boolean,
            isJumper: Boolean
        ): Boolean
    }

    data class StateHolder(
        val model: BottomNavBarUiModel,
        val isSelected: Boolean?,
        val isJumper: Boolean = false
    ) {
        companion object {
            fun init(model: BottomNavBarUiModel): StateHolder {
                return StateHolder(model, null)
            }
        }
    }

    private sealed interface AssetPlaylist

    @JvmInline
    private value class ImagePlaylist(val image: Type.Image) : AssetPlaylist {
        companion object {
            fun of(type: Type?): ImagePlaylist? {
                return if (type is Type.Image) ImagePlaylist(type) else null
            }
        }
    }

    private data class LottiePlaylist(
        val firstAsset: Type.Lottie,
        val nextAsset: Type.Lottie?,
        val isInfinite: Boolean
    ) : AssetPlaylist {
        companion object {
            fun of(firstAsset: Type?, secondAsset: Type? = null, isInfinite: Boolean = false): LottiePlaylist? {
                return if (firstAsset !is Type.Lottie) {
                    null
                } else {
                    LottiePlaylist(
                        firstAsset,
                        secondAsset as? Type.Lottie,
                        isInfinite
                    )
                }
            }
        }
    }
}
