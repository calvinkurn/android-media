package com.tokopedia.product.detail.view.fragment.fpi.manually

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnDrawListener
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.core.view.children
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Created by yovi.putra on 06/07/23"
 * Project name: android-tokopedia-core
 **/


abstract class BaseFpiDrawFragment : BaseDaggerFragment() {

    private val viewTreeMap = mutableMapOf<String, Set<Long>>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            (view as? ViewGroup)?.let {
                getAllViews(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewTreeMap.forEach {
            Timber.tag(javaClass.simpleName).d("${it.key} -> ${it.value}ms")
        }
    }

    private fun getAllViews(view: ViewGroup) {
        view.children.forEach {

            it.addOnViewMeasureRenderingTime { viewName, duration ->
                val previous = viewTreeMap[viewName].orEmpty().toMutableSet()
                previous.add(duration)
                viewTreeMap[viewName] = previous
            }

            if (it is ViewGroup) {
                getAllViews(it)
            }
        }
    }

    private fun View.addOnViewMeasureRenderingTime(result: (viewName: String, duration: Long) -> Unit) {
        ViewDrawing(viewRef = WeakReference(this), result = result)
    }

    inner class ViewDrawing(
        val viewRef: WeakReference<View>,
        val result: (viewName: String, duration: Long) -> Unit
    ) {

        private val view by lazy { viewRef.get() }
        private var startTime = 0L

        private val preDrawListener = OnPreDrawListener {
            startTime = System.currentTimeMillis()
            true
        }

        private val drawListener = object : OnDrawListener {
            override fun onDraw() {
                if (startTime == 0L) return
                val view = view ?: return
                view.viewTreeObserver?.removeOnPreDrawListener(preDrawListener)
                val mContext = view.context ?: return

                val total = System.currentTimeMillis() - startTime
                val viewName = runCatching {
                    mContext.resources.getResourceName(view.id)
                }.getOrDefault("unknown")
                result(viewName, total)
                startTime = 0
            }
        }

        init {
            view?.viewTreeObserver?.apply {
                addOnPreDrawListener(preDrawListener)
                addOnDrawListener(drawListener)
            }
        }
    }
}
