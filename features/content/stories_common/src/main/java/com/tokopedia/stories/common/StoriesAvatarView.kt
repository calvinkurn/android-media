package com.tokopedia.stories.common

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(
    context,
    attrs,
    defStyleAttr
) {

    private var mImageUrl by mutableStateOf("")
    private var storiesStatus by mutableStateOf(StoriesStatus.NoStories)
    private var mSizeConfiguration by mutableStateOf(SizeConfiguration.Default)

    private val shopId = MutableStateFlow("2")

    private var mViewModel: StoriesAvatarViewModel? = null
    private var observeJob: Job? = null

    private var mOnNoStoriesClicked: OnClickListener? = null

    private val coachMark = CoachMark2(context)

    init {
        super.setOnClickListener {
            mViewModel?.onIntent(StoriesAvatarIntent.OpenStoriesDetail(shopId.value))
        }
    }

    @Composable
    override fun Content() {
        StoriesAvatarContent(
            mImageUrl,
            storiesStatus,
            sizeConfig = mSizeConfiguration,
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        observeJob?.cancel()
        mViewModel = null
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnNoStoriesClicked = l
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    fun updateSizeConfig(update: (SizeConfiguration) -> SizeConfiguration) {
        mSizeConfiguration = update(mSizeConfiguration)
    }

    fun associateToShopId(shopId: String) {
        this.shopId.update { shopId }
    }

    internal fun attach(viewModel: StoriesAvatarViewModel) {
        mViewModel = viewModel
        startObserve(viewModel)
    }

    internal fun detach() {
        mViewModel = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startObserve(viewModel: StoriesAvatarViewModel) {
        val lifecycleOwner = getLifecycleOwner() ?: return
        observeJob = lifecycleOwner.lifecycleScope.launch {
            launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    shopId.flatMapLatest {
                        viewModel.getStoriesState(it)
                    }.collectLatest {
                        storiesStatus = it?.status ?: StoriesStatus.NoStories
                    }
                }
            }

            launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    shopId.flatMapLatest {
                        viewModel.getStoriesMessage(it)
                    }.collect { message ->
                        if (message == null) return@collect

                        when (message) {
                            is StoriesAvatarMessage.OpenStoriesDetail -> {
                                RouteManager.route(context, message.appLink)
                            }
                            is StoriesAvatarMessage.OpenDetailWithNoStories -> {
                                mOnNoStoriesClicked?.onClick(this@StoriesAvatarView)
                            }
                            is StoriesAvatarMessage.ShowCoachMark -> {
                                showCoachMark()
                            }
                        }

                        viewModel.clearMessage(message.id)
                    }
                }
            }
        }
    }

    private fun getLifecycleOwner(): LifecycleOwner? {
        return findViewTreeLifecycleOwner()
    }

    private fun showCoachMark() {
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(this, "Ada update menarik dari toko ini", "")
            )
        )
    }

    data class SizeConfiguration(
        val imageToBorderGap: Dp,
    ) {
        companion object {
            val Default: SizeConfiguration
                get() = SizeConfiguration(
                    imageToBorderGap = 8.dp
                )
        }
    }
}
