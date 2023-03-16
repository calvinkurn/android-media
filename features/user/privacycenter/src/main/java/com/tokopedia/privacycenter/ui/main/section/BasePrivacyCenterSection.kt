package com.tokopedia.privacycenter.ui.main.section

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionBaseBinding

abstract class BasePrivacyCenterSection(context: Context?) {

    open val sectionViewBinding: ViewBinding? = null
    open val sectionTextTitle: String = ""
    open val sectionTextDescription: String = ""
    open val isShowDirectionButton: Boolean = false
    open val isShowDivider: Boolean = true
    open fun initObservers() {}
    open fun onViewRendered() {}
    open fun onButtonDirectionClick(view: View) {}

    var lifecycleOwner: LifecycleOwner? = context as? LifecycleOwner

    private var sectionBaseViewBinding: SectionBaseBinding = SectionBaseBinding.inflate(
        LayoutInflater.from(context)
    )

    private fun initBaseView() {
        initDirectionButton()

        sectionBaseViewBinding.apply {
            sectionTitle.text = sectionTextTitle
            sectionSubtitle.apply {
                text = sectionTextDescription
            }.showWithCondition(sectionTextDescription.isNotEmpty())

            if (sectionViewBinding != null) {
                sectionContent.addView(sectionViewBinding?.root)
            } else {
                sectionContentLayout.hide()
            }

            sectionDivider.showWithCondition(isShowDivider)
        }
    }

    private fun initDirectionButton() {
        sectionBaseViewBinding.sectionButtonDirection.showWithCondition(isShowDirectionButton)
        sectionBaseViewBinding.sectionHeader.setOnClickListener {
            if (isShowDirectionButton) {
                onButtonDirectionClick(it)
            }
        }
    }

    fun getView(): View {
        initObservers()
        initBaseView()
        showShimmering(true)
        onViewRendered()
        return sectionBaseViewBinding.root
    }

    fun showShimmering(isLoading: Boolean) {
        sectionViewBinding?.root?.showWithCondition(!isLoading)
        sectionBaseViewBinding.apply {
            baseSection.showWithCondition(!isLoading)
            loadingView.root.showWithCondition(isLoading)
            if (isLoading) sectionLocalLoad.hide()
        }
    }

    fun showLocalLoad(title: String = "", description: String = "", onRetryClick: (View) -> Unit) {
        sectionViewBinding?.root?.hide()
        sectionBaseViewBinding.apply {
            loadingView.root.hide()
            baseSection.show()
        }

        sectionBaseViewBinding.sectionLocalLoad.apply {
            localLoadTitle = title.ifEmpty {
                context?.resources?.getString(R.string.privacy_center_error_network_title).orEmpty()
            }
            localLoadDescription = description
            refreshBtn?.setOnClickListener {
                this.hide()
                onRetryClick.invoke(it)
            }
        }.show()
    }

    fun hideLocalLoad() {
        sectionBaseViewBinding.sectionLocalLoad.hide()
    }
}
