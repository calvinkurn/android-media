package com.tokopedia.usercomponents.tokopediaplus.ui

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.usercomponents.databinding.UiTokopediaPlusBinding
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusCons
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

class TokopediaPlusWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttr) {

    companion object {
        private const val HEIGHT_NON_SUBSCRIBER = 56f
        private const val HEIGHT_SUBSCRIBER = 40f
    }

    private var pageSource: String = ""
    var listener: TokopediaPlusListener? = null

    private var viewBinding: UiTokopediaPlusBinding =
        UiTokopediaPlusBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        ).also {
            addView(it.root)
        }

    fun setContent(
        param: TokopediaPlusParam
    ) {
        pageSource = param.pageSource

        hideLoading()
        if (pageSource == TokopediaPlusCons.SOURCE_ACCOUNT_PAGE) {
            renderViewCard(param.tokopediaPlusDataModel)
        } else {
            renderView(param.tokopediaPlusDataModel)
        }
    }

    fun onError(throwable: Throwable? = null) {
        viewBinding.apply {
            tokopediaPlusComponent.root.hide()
            tokopediaPlusCardComponent.root.hide()
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
            if (tokopediaPlusData.title.isNotEmpty() && tokopediaPlusData.iconImageURL.isNotEmpty()) {
                listener?.isShown(tokopediaPlusData.isShown, pageSource, tokopediaPlusDataModel)
                viewBinding.apply {
                    tokopediaPlusCardComponent.root.hide()
                    root.visibility = if (!tokopediaPlusData.isShown) GONE else VISIBLE

                    tokopediaPlusComponent.apply {
                        val containerHeight =  if(tokopediaPlusData.isSubscriber) {
                            HEIGHT_SUBSCRIBER.dpToPx().toInt()
                        } else HEIGHT_NON_SUBSCRIBER.dpToPx().toInt()
                        containerTokopediaPlus.layoutParams.height = containerHeight
                        iconTokopediaPlus.loadIcon(tokopediaPlusData.iconImageURL)
                        titleTokopediaPlus.text = tokopediaPlusData.title
                        descriptionTokopediaPlus.text = MethodChecker.fromHtml(tokopediaPlusData.subtitle)
                        descriptionTokopediaPlus.visibility = if (tokopediaPlusData.isSubscriber) GONE else VISIBLE

                        setOnClickListener {
                            listener?.onClick(pageSource, tokopediaPlusData)

                            val intent = RouteManager.getIntent(context, tokopediaPlusData.applink)
                            context.startActivity(intent)
                        }
                    }.root.show()
                }
            } else {
                viewBinding.root.hide()
            }
        }
    }

    private fun renderViewCard(tokopediaPlusDataModel: TokopediaPlusDataModel?) {
        tokopediaPlusDataModel?.let { tokopediaPlusData ->
            if (tokopediaPlusData.title.isNotEmpty() && tokopediaPlusData.iconImageURL.isNotEmpty()) {
                listener?.isShown(tokopediaPlusData.isShown, pageSource, tokopediaPlusDataModel)
                viewBinding.apply {
                    tokopediaPlusComponent.root.hide()
                    root.visibility = if (!tokopediaPlusData.isShown) GONE else VISIBLE

                    tokopediaPlusCardComponent.apply {
                        val containerHeight =  if(tokopediaPlusData.isSubscriber) {
                            HEIGHT_SUBSCRIBER.dpToPx().toInt()
                        } else HEIGHT_NON_SUBSCRIBER.dpToPx().toInt()
                        containerTokopediaPlus.layoutParams.height = containerHeight
                        iconTokopediaPlus.loadIcon(tokopediaPlusData.iconImageURL)
                        titleTokopediaPlus.text = tokopediaPlusData.title
                        descriptionTokopediaPlus.text = MethodChecker.fromHtml(tokopediaPlusData.subtitle)
                        descriptionTokopediaPlus.visibility = if (tokopediaPlusData.isSubscriber) GONE else VISIBLE

                        setOnClickListener {
                            listener?.onClick(pageSource, tokopediaPlusData)

                            val intent = RouteManager.getIntent(context, tokopediaPlusData.applink)
                            context.startActivity(intent)
                        }
                    }.root.show()
                }
            } else {
                viewBinding.root.hide()
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