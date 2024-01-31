package com.tokopedia.shop_widget.buy_more_save_more.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextSwitcher
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by @ilhamsuaib on 26/09/23.
 */

class SlidingTextSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextSwitcher(context, attrs), CoroutineScope {

    companion object {
        private const val MESSAGE_SWITCH_INITIAL_DELAY = 3000L
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var currentRollingTextIndex = 0

    private var job: Job? = null

    private var currentTitle = ""

    init {
        setFactory {
            return@setFactory createTypography()
        }
    }

    fun setMessages(
        messages: List<String>,
        interval: Long = MESSAGE_SWITCH_INITIAL_DELAY,
        textColor: Int
    ) {
        if (messages.size > Int.ONE) {
            visible()
            startRollingTitle(titles = messages, interval = interval)
        } else {
            val message = messages.firstOrNull().orEmpty()
            showSingleMessageWithNoAnimation(message)
        }
        val typography = currentView as Typography
        typography.setTextColor(textColor)
    }

    private fun showSingleMessageWithNoAnimation(message: String) {
        if (message.isBlank()) {
            gone()
        } else {
            visible()
            setCurrentText(message.parseAsHtml())
        }
    }


    private fun startRollingTitle(titles: List<String>, interval: Long) {
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
            while (currentRollingTextIndex < titles.size) {
                val title = titles[currentRollingTextIndex]

                if (currentTitle != title) {
                    currentTitle = title
                    withContext(Dispatchers.Main) {
                        setText(title.parseAsHtml())
                    }
                }

                delay(interval)
                currentRollingTextIndex++
            }
            clear()
        }
    }

    private fun clear() {
        currentRollingTextIndex = 0
        job?.cancel()
    }

    private fun createTypography() =
        Typography(context).apply {
            setType(Typography.SMALL)
        }
}
