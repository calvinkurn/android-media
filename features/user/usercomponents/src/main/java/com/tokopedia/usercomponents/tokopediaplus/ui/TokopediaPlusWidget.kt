package com.tokopedia.usercomponents.tokopediaplus.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.usercomponents.databinding.UiTokopediaPlusBinding
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

class TokopediaPlusWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var listener: TokopediaPlusListener? = null
    private var pageSource: String = ""

    private var viewBinding: UiTokopediaPlusBinding =
        UiTokopediaPlusBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        ).also {
            addView(it.root)
        }

    var icon = ""
        set(value) {
            viewBinding.tokopediaPlusComponent.iconTokopediaPlus.loadIcon(value)
        }

    var title = ""
        set(value) {
            viewBinding.tokopediaPlusComponent.titleTokopediaPlus.text = value
            field = value
        }

    var subtitle = ""
        set(value) {
            viewBinding.tokopediaPlusComponent.descriptionTokopediaPlus.text = MethodChecker.fromHtml(value)
            field = value
        }

    fun setContent(
        param: TokopediaPlusParam,
        listener: TokopediaPlusListener
    ) {
        pageSource = param.pageSource
        this.listener = listener

        hideLoading()
        renderView(param.tokopediaPlusDataModel)
    }

    fun onError(throwable: Throwable) {
        viewBinding.apply {
            tokopediaPlusComponent.root.hide()
            tokopediaPlusLoader.root.hide()

            tokopediaLocalLoad.show()
            tokopediaLocalLoad.setOnClickListener {
                listener?.onRetry()
            }
        }
    }

    fun showLoading() {
        showLoading(true)
    }

    fun hideLoading() {
        showLoading(false)
    }

    private fun renderView(tokopediaPlusDataModel: TokopediaPlusDataModel?) {
        tokopediaPlusDataModel?.let { tokopediaPlusData ->
            icon = tokopediaPlusData.iconImageURL
            title = tokopediaPlusData.title
            subtitle = tokopediaPlusData.subtitle

            viewBinding.apply {
                if (!tokopediaPlusData.isShown) {
                    root.hide()
                } else {
                    tokopediaPlusComponent.apply {
                        descriptionTokopediaPlus.visibility =
                            if (tokopediaPlusData.isSubscriber) GONE
                            else VISIBLE

                        iconDirectionTokopediaPlusPage.setOnClickListener {
                            listener?.onClick(pageSource, tokopediaPlusData)

                            val intent = RouteManager.getIntent(context, tokopediaPlusData.applink)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isShown: Boolean) {
        viewBinding.apply {
            tokopediaLocalLoad.hide()

            if (isShown) {
                tokopediaPlusLoader.root.show()
                tokopediaPlusComponent.root.hide()
            } else {
                tokopediaPlusLoader.root.hide()
                tokopediaPlusComponent.root.show()
            }
        }
    }

}