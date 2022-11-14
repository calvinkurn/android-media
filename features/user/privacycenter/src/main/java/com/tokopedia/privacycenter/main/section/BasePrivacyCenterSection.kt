package com.tokopedia.privacycenter.main.section

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.privacycenter.databinding.SectionBaseBinding

abstract class BasePrivacyCenterSection(context: Context?) {

    protected abstract val sectionViewBinding: ViewBinding
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
            sectionSubtitle.text = sectionTextDescription.orEmpty()
            sectionContent.addView(sectionViewBinding.root)
        }
    }

    private fun initDirectionButton() {
        sectionBaseViewBinding.sectionButtonDirection.apply {
            if (isShowDirectionButton) show() else hide()

            setOnClickListener {
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
        if (isLoading) {
            sectionViewBinding.root.hide()
            sectionBaseViewBinding.apply {
                baseSection.hide()
                loadingView.root.show()
            }
        } else {
            sectionViewBinding.root.show()
            sectionBaseViewBinding.apply {
                baseSection.show()
                loadingView.root.hide()
            }
        }
    }

    fun showLocalLoad(title: String, description: String, onRetryClick: (View) -> Unit) {
        sectionViewBinding.root.hide()
        showShimmering(false)

        sectionBaseViewBinding.sectionLocalLoad.apply {
            localLoadTitle = title
            localLoadDescription = description
            refreshBtn?.setOnClickListener {
                progressState = true
                onRetryClick.invoke(it)
            }
        }.show()
    }
}

