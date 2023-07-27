package com.tokopedia.stories.common

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.stories.common.di.DaggerStoriesAvatarComponent
import com.tokopedia.stories.common.di.StoriesAvatarComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
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
    private var storiesStatus by mutableStateOf(StoriesStatus.HasUnseenStories)

    private val shopId = MutableStateFlow("2")

    private var mViewModel: StoriesAvatarViewModel? = null
    private var observeJob: Job? = null

    private var mOnNoStoriesClicked: OnClickListener? = null

    private val coachMark = CoachMark2(context)

    private val component = createComponent()
    private val viewModelFactory = component.viewModelFactory()

    init {
        super.setOnClickListener {
            mViewModel?.onIntent(StoriesAvatarIntent.OpenStoriesDetail(shopId.value))
        }
    }

    @Composable
    override fun Content() {
        StoriesAvatarContent(mImageUrl, storiesStatus)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        mViewModel = getViewModel()
        startObserve()

        updateStoriesStatus()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        observeJob?.cancel()
        mViewModel = null
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnNoStoriesClicked = l
    }

    fun updateStoriesStatus() {
        mViewModel?.onIntent(StoriesAvatarIntent.GetStoriesStatus(shopId.value))
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startObserve() {
        val viewModel = mViewModel ?: return
        val lifecycleOwner = getLifecycleOwner() ?: return
        observeJob = lifecycleOwner.lifecycleScope.launch {
            launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    shopId.flatMapLatest {
                        viewModel.getStoriesState(it)
                    }.collectLatest {
                        storiesStatus = it.status
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

    private fun getViewModel(): StoriesAvatarViewModel? {
        val viewModelStoreOwner = findViewTreeViewModelStoreOwner() ?: return null
        return ViewModelProvider(viewModelStoreOwner, viewModelFactory).get()
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

    private fun createComponent(): StoriesAvatarComponent {
        return DaggerStoriesAvatarComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }
}
