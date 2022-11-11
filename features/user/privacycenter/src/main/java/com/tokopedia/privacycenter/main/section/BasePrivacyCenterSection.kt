package com.tokopedia.privacycenter.main.section

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

    protected abstract val sectionViewBinding: ViewBinding?
    protected abstract val sectionTextTitle: String?
    protected abstract val sectionTextDescription: String?
    protected abstract val isShowDirectionButton: Boolean
    protected abstract fun initObservers()
    protected abstract fun onViewRendered()
    protected abstract fun onButtonDirectionClick(view: View)

    val lifecycleOwner: LifecycleOwner? = context as? LifecycleOwner

    private var sectionBaseViewBinding: SectionBaseBinding = SectionBaseBinding.inflate(
        LayoutInflater.from(context)
    )

    private fun initBaseView() {
        initDirectionButton()

        sectionBaseViewBinding.apply {
            sectionTitle.text = sectionTextTitle.orEmpty()
            sectionSubtitle.apply {
                text = sectionTextDescription
            }.showWithCondition(sectionTextDescription?.isNotEmpty() == true)

            if (sectionViewBinding != null) {
                sectionContent.addView(sectionViewBinding?.root)
            }
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
        }
    }

    fun showLocalLoad(title: String = "", description: String = "", onRetryClick: (View) -> Unit) {
        sectionViewBinding?.root?.hide()
        showShimmering(false)

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

