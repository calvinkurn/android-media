package com.tokopedia.product.detail.common.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextSwitcher
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.stringToUnifyColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

class RollingTextSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextSwitcher(context, attrs), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    // region field need for rolling text
    private val textViews = mutableListOf<Typography>()
    private var job: Job? = null
    private var currentIndexRollingText = 0
    private var isRunning = true
    private var currentTitle = ""
    // endregion

    // region mandatory parameter needed from caller
    private var titles: List<String> = emptyList()
    private var color: String = ""
    private var interval: Long = 0
    // endregion

    init {
        setFactory {
            createTypography().also {
                textViews.add(it)
            }
        }
    }

    fun setTitle(titles: List<String>, color: String, interval: Long) {
        this.titles = titles
        this.color = color
        this.interval = interval

        setTextColor()
        startRollingTitle()
    }

    private fun startRollingTitle() {
        // when refresh page data
        clear()

        job = launch {
            runCatching {
                rollingTitle(titles = titles, interval = interval)
            }
        }
    }

    private suspend fun rollingTitle(titles: List<String>, interval: Long) {
        withContext(Dispatchers.IO) {
            while (isRunning) {
                val title = titles[currentIndexRollingText]

                if (currentTitle != title) {
                    currentTitle = title
                    withContext(Dispatchers.Main) {
                        setText(title)
                    }
                }

                delay(interval)
                incrementIndex(size = titles.size)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (!isRunning) {
            startRollingTitle()
        }
    }

    override fun onDetachedFromWindow() {
        pause()
        super.onDetachedFromWindow()
    }

    private fun pause() {
        isRunning = false
        job?.cancel()
    }

    private fun clear() {
        isRunning = true
        job?.cancel()
    }

    private fun incrementIndex(size: Int) {
        currentIndexRollingText = (currentIndexRollingText + 1) % size
    }

    private fun createTypography() = Typography(context).apply {
        setType(Typography.PARAGRAPH_3)
        setWeight(Typography.BOLD)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    private fun setTextColor() {
        textViews.forEach {
            val default = com.tokopedia.unifyprinciples.R.color.Unify_TN500
            val unifyColor = getStringUnifyColor(color, default)
            it.setTextColor(unifyColor)
        }
    }

    private fun getStringUnifyColor(color: String, default: Int) = runCatching {
        stringToUnifyColor(context, color).unifyColor
    }.getOrNull() ?: runCatching {
        Color.parseColor(color)
    }.getOrNull() ?: default
}
