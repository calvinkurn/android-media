package com.tokopedia.stories.common

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.AttributeSet
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.applink.RouteManager
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
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
                        }

                        viewModel.clearMessage(message.id)
                    }
                }
            }
        }
    }

    private fun getViewModel(): StoriesAvatarViewModel? {
        val viewModelStoreOwner = findViewTreeViewModelStoreOwner() ?: return null
        return ViewModelProvider(viewModelStoreOwner).get()
    }

    private fun getLifecycleOwner(): LifecycleOwner? {
        return findViewTreeLifecycleOwner()
    }
}

@Composable
private fun StoriesAvatarContent(
    imageUrl: String,
    storiesStatus: StoriesStatus,
    modifier: Modifier = Modifier,
    imageToBorderPadding: Dp = 8.dp
) {
    NestTheme {
        Box(
            modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = storiesStatus.borderDp,
                    brush = storiesStatus.brush,
                    shape = CircleShape
                )
        ) {
            NestImage(
                imageUrl = imageUrl,
                Modifier
                    .matchParentSize()
                    .padding(imageToBorderPadding)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NoStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.NoStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HasUnseenStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.HasUnseenStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AllStoriesSeenAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.AllStoriesSeen,
        Modifier.size(200.dp)
    )
}

private val StoriesStatus.borderDp: Dp
    get() = when (this) {
        StoriesStatus.NoStories -> 1.dp
        StoriesStatus.HasUnseenStories -> 2.dp
        StoriesStatus.AllStoriesSeen -> 1.dp
    }

private val StoriesStatus.brush: Brush
    get() = when (this) {
        StoriesStatus.NoStories -> SolidColor(Color(0x00000000))
        StoriesStatus.HasUnseenStories -> {
            Brush.linearGradient(
                0f to Color(0xFF83ECB2),
                1f to Color(0xFF00AA5B)
            )
        }
        StoriesStatus.AllStoriesSeen -> SolidColor(Color(0xFFD6DFEB))
    }
