package com.tokopedia.shop_widget.buy_more_save_more.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextSwitcher
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop_widget.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Method
import kotlin.coroutines.CoroutineContext

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

    var titles = mutableListOf<String>()

    var textColor = MethodChecker.getColor(context, R.color.dms_static_white)

    fun setMessages(
        messages: List<String>,
        interval: Long = MESSAGE_SWITCH_INITIAL_DELAY,
        textColor: Int
    ) {
        titles.clear()
        titles.addAll(messages)

        if (titles.size > Int.ONE) {
            visible()
            if (currentTitle.isNotEmpty()) setText(titles.firstOrNull())
            startRollingTitle(titles = titles, interval = interval, textColor)
        } else {
            val message = titles.firstOrNull().orEmpty()
            showSingleMessageWithNoAnimation(message)
        }
    }

    private fun showSingleMessageWithNoAnimation(message: String) {
        if (message.isEmpty()) {
            gone()
        } else {
            visible()
            setCurrentText(MethodChecker.fromHtml(message))
        }
    }


    private fun startRollingTitle(titles: List<String>, interval: Long, textColor: Int) {
        // when refresh page data
        clear()

        job = launch {
            runCatching {
                rollingTitle(titles = titles, interval = interval, textColor)
            }
        }
    }

    private suspend fun rollingTitle(titles: List<String>, interval: Long, textColor: Int) {
        withContext(Dispatchers.IO) {
            while (currentRollingTextIndex < titles.size) {
                val title = titles[currentRollingTextIndex]

                if (currentTitle != title) {
                    currentTitle = title
                    withContext(Dispatchers.Main) {
                        setCurrentText("")
                        setText(MethodChecker.fromHtml(title))
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
}
