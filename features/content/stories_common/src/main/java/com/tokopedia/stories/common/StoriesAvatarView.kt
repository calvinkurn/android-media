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
import com.tokopedia.applink.RouteManager

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
    private var mState by mutableStateOf(StoriesAvatarState.Default)
    private var mSizeConfiguration by mutableStateOf(SizeConfiguration.Default)

    private var mOnNoStoriesClicked: OnClickListener? = null

    init {
        super.setOnClickListener {
            when (mState.status) {
                StoriesStatus.NoStories -> {
                    mOnNoStoriesClicked?.onClick(this)
                }
                else -> {
                    RouteManager.route(context, mState.appLink)
                }
            }
        }
    }

    @Composable
    override fun Content() {
        StoriesAvatarContent(
            mImageUrl,
            mState.status,
            sizeConfig = mSizeConfiguration
        )
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnNoStoriesClicked = l
    }

    fun setState(onUpdate: (StoriesAvatarState) -> StoriesAvatarState) {
        mState = onUpdate(mState)
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }

    fun updateSizeConfig(update: (SizeConfiguration) -> SizeConfiguration) {
        mSizeConfiguration = update(mSizeConfiguration)
    }

    data class SizeConfiguration(
        val imageToBorderGap: Dp,
        val unseenStoriesBorder: Dp
    ) {
        companion object {
            val Default: SizeConfiguration
                get() = SizeConfiguration(
                    imageToBorderGap = 4.dp,
                    unseenStoriesBorder = 3.dp,
                )
        }
    }
}
