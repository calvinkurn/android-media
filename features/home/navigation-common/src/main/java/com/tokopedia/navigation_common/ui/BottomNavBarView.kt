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
import com.airbnb.lottie.LottieListener
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

    /**
     * Set jumper's state for given [BottomNavItemId].
     * Once the state really changes, the bottom nav ui will immediately be refreshed
     * to reflect the newest jumper state
     *
     * @param id the id of the bottom nav item to which jumper state wanted to be set on
     * @param shouldChangeToJumper whether the corresponding bottom nav item should use jumper asset
     */
    fun setJumperForId(id: BottomNavItemId, shouldChangeToJumper: Boolean) {
        val oldState = jumperStateMap[id]
        jumperStateMap[id] = shouldChangeToJumper
        if (oldState != shouldChangeToJumper) refresh()
    }

    /**
     * Set models for the bottom nav. By default if no previous bottom nav item was selected,
     * the first item in the models will automatically be selected.
     *
     * @param modelList the models of the bottom nav to be applied on the [BottomNavBarView]
     */
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
        selectInternal(mSelectedItemId ?: firstModel.uniqueId)
    }

    /**
     * Select the specified [BottomNavItemId] to be active.
     *
     * Selection can be forced which means even though the current models do not contain the specified [BottomNavItemId],
     * it will still be forcefully selected internally. This might be useful if
     * you need to select a [BottomNavItemId] while still waiting for the models.
     * After the models have been set, it will automatically select the id specified in this function.
     *
     * A non-forced selection will ignore the selection process if the model
     * with the specified id does not exist.
     *
     * Note that the [BottomNavBarView.Listener.onItemSelected] will always be called if the model existed
     * regardless the selection is forced or not.
     *
     * Selecting item through this method will not trigger any reselection
     * even though the current item id is the same as the specified [itemId]
     *
     * @param itemId the id to be selected
     * @param forceSelect whether the selection should be forced
     */
    fun select(itemId: BottomNavItemId, forceSelect: Boolean = false) {
        selectInternal(itemId, forceSelect, shouldTriggerReselection = false)
    }

    /**
     * Select the specified [BottomNavItemId] to be active.
     *
     * Selection can be forced which means even though the current models do not contain the specified [BottomNavItemId],
     * it will still be forcefully selected internally. This might be useful if
     * you need to select a [BottomNavItemId] while still waiting for the models.
     * After the models have been set, it will automatically select the id specified in this function.
     *
     * A non-forced selection will ignore the selection process if the model
     * with the specified id does not exist.
     *
     * Note that the [BottomNavBarView.Listener.onItemSelected] will always be called if the model existed
     * regardless the selection is forced or not.
     *
     * @param itemId the id to be selected
     * @param forceSelect whether the selection should be forced
     * @param shouldTriggerReselection whether calling this should trigger reselection if the current item id is the same as the [itemId]
     */
    private fun selectInternal(itemId: BottomNavItemId, forceSelect: Boolean = false, shouldTriggerReselection: Boolean = true) {
        val model = modelMap[itemId]

        if (model != null) {
            val isSelected = mListener?.onItemSelected(this, model, mSelectedItemId == itemId && shouldTriggerReselection, model.isJumper())
            if (isSelected != true && !forceSelect) return
        } else {
            if (!forceSelect) return
        }

        mSelectedItemId = itemId
        rebindAllItems(itemId)
    }

    /**
     * Allows the view to forcefully use dark mode
     * @param shouldDarkMode whether it should forcefully use dark mode, otherwise it will follow device ui mode
     */
    fun forceDarkMode(shouldDarkMode: Boolean) {
        if (shouldDarkMode == mIsForceDarkMode) return
        mIsForceDarkMode = shouldDarkMode
        refresh()
    }

    /**
     * Set the listener to this [BottomNavBarView]
     */
    fun setListener(listener: Listener?) {
        mListener = listener
    }

    /**
     * Find the corresponding [View] for the given [BottomNavItemId]
     *
     * @param id the inputted id
     * @return the corresponding [View] if found, null otherwise.
     */
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
        root.setOnClickListener { selectInternal(model.uniqueId) }

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
        clearLottieTask()
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
        setLottieTaskFromAsset(playlist.firstAsset) {
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
                setLottieTaskFromAsset(playlist.nextAsset) {
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

    private fun LottieAnimationView.setLottieTaskFromAsset(
        asset: Type.Lottie,
        listener: LottieListener<LottieComposition>
    ) {
        clearLottieTask()
        lottieTaskListener = listener
        lottieTask = asset.toLottieTask().addListener(listener)
    }

    private fun LottieAnimationView.clearLottieTask() {
        lottieTask?.removeListener(lottieTaskListener)
        lottieTaskListener = null
        lottieTask = null
    }

    private var LottieAnimationView.lottieTask: LottieTask<LottieComposition>?
        get() {
            return getTag(navigation_commonR.id.bottom_nav_lottie_task) as? LottieTask<LottieComposition>
        }
        set(value) {
            setTag(navigation_commonR.id.bottom_nav_lottie_task, value)
        }

    private var LottieAnimationView.lottieTaskListener: LottieListener<LottieComposition>?
        get() {
            return getTag(navigation_commonR.id.bottom_nav_lottie_listener) as? LottieListener<LottieComposition>
        }
        set(value) {
            setTag(navigation_commonR.id.bottom_nav_lottie_listener, value)
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
