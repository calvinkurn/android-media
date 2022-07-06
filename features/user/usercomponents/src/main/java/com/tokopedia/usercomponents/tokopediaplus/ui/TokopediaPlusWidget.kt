package com.tokopedia.usercomponents.tokopediaplus.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.databinding.UiTokopediaPlusBinding
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.usercomponents.tokopediaplus.di.DaggerTokopediaPlusComponent
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import javax.inject.Inject

class TokopediaPlusWidget: ConstraintLayout {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private var lifecycleOwner: LifecycleOwner? = null
    private var viewModel: TokopediaPlusViewModel? = null
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

    constructor(context: Context) : super(context) {
        initInjector()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initInjector()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        initInjector()
    }

    fun load(
        param: TokopediaPlusParam,
        listener: TokopediaPlusListener
    ) {
        pageSource = param.pageSource

        this.lifecycleOwner = param.lifecycleOwner
        this.listener = listener

        initViewModel(param.viewModeStrStoreOwner)
        initObserver()

        viewModel?.loadTokopediPlus(param.pageSource)
    }

    private fun initInjector() {
        context?.let {
            DaggerTokopediaPlusComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner) {
        val viewModelProvider = ViewModelProvider(viewModelStoreOwner, viewModelProvider)
        viewModel = viewModelProvider.get(TokopediaPlusViewModel::class.java)
    }

    private fun initObserver() {
        lifecycleOwner?.let {
            viewModel?.tokopedisPlus?.observe(it) { result ->
                when (result) {
                    is UserComponentsStateResult.Loading -> showLoading(true)
                    is UserComponentsStateResult.Success -> {
                        showLoading(false)
                        onSuccessGetData(result.data)
                    }
                    is UserComponentsStateResult.Fail -> {
                        showLoading(false)
                        onFailedGetData(result.error)
                    }
                }
            }
        }
    }

    private fun onSuccessGetData(tokopediaPlusDataModel: TokopediaPlusDataModel?) {
        tokopediaPlusDataModel?.let { tokopediaPlusData ->
            listener?.onSuccessLoad(pageSource, tokopediaPlusData)

            icon = tokopediaPlusData.iconImageURL
            title = tokopediaPlusData.title
            subtitle = tokopediaPlusData.subtitle

            viewBinding.apply {
                if (!tokopediaPlusData.isShown) {
                    root.hide()
                } else {
                    tokopediaPlusComponent.apply {
                        if (tokopediaPlusData.isSubscriber) {
                            iconTokopediaPlus.layoutParams.width = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toIntSafely()
                            iconTokopediaPlus.layoutParams.height = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toIntSafely()

                            descriptionTokopediaPlus.hide()
                        } else {
                            iconTokopediaPlus.layoutParams.width = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl4).toIntSafely()
                            iconTokopediaPlus.layoutParams.height = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl4).toIntSafely()

                            descriptionTokopediaPlus.show()
                        }

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

    private fun onFailedGetData(throwable: Throwable) {
        listener?.onFailedLoad(throwable)

        viewBinding.apply {
            tokopediaLocalLoad.show()
            tokopediaPlusComponent.root.hide()
            tokopediaPlusLoader.root.hide()
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

    override fun onDetachedFromWindow() {
        lifecycleOwner?.let {
            viewModel?.tokopedisPlus?.removeObservers(it)
        }

        lifecycleOwner = null
        super.onDetachedFromWindow()
    }
}