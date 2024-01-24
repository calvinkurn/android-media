package com.tokopedia.shareexperience.ui.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.databinding.ShareexperienceLoadingOverlayBinding
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@SuppressLint("UnifyComponentUsage")
class ShareExLoadingDialog(
    context: Context,
    private val id: String,
    private val pageTypeEnum: ShareExPageTypeEnum,
    private val onResult: (ShareExResult<ShareExBottomSheetModel>) -> Unit
): Dialog(context, R.style.CustomDialogTheme) {

    private val weakContext = WeakReference(context)
    private var _binding: ShareexperienceLoadingOverlayBinding? = null

    @Inject
    lateinit var useCase: ShareExGetSharePropertiesUseCase

    init {
        initInjector()
        setBinding()
        setListener()
        fetchBottomSheetData()
    }

    private fun initInjector() {
        val baseMainApplication = weakContext.get()?.applicationContext as? BaseMainApplication
        baseMainApplication?.let {
            DaggerShareExComponent
                .builder()
                .baseAppComponent(it.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun setBinding() {
        _binding = ShareexperienceLoadingOverlayBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun setListener() {
        _binding?.root?.setOnClickListener {
            this.dismiss()
        }
    }

    private fun fetchBottomSheetData() {
        weakContext.get()?.let {
            (it as? FragmentActivity)?.lifecycleScope?.launch {
                try {
                    val request = ShareExProductBottomSheetRequest(
                        pageType = pageTypeEnum.valueInt,
                        id = id
                    )
                    useCase.getData(request).collectLatest { result ->
                        when (result) {
                            is ShareExResult.Success, is ShareExResult.Error -> {
                                onResult(result)
                            }
                            ShareExResult.Loading -> Unit
                        }
                    }
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    onResult(ShareExResult.Error(throwable))
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
