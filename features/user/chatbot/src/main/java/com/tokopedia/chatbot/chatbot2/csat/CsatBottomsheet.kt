package com.tokopedia.chatbot.chatbot2.csat

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chatbot.chatbot2.csat.di.CsatComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.csat.di.DaggerCsatComponent
import com.tokopedia.chatbot.databinding.BottomsheetCsatBinding

class CsatBottomsheet :
    BottomSheetUnify(),
    HasComponent<CsatComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding: BottomsheetCsatBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = BottomsheetCsatBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        super.onDismiss(dialog)
    }

    private fun initializeBottomSheet() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true
        showKnob = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setChild(viewBinding?.root)
    }

    override fun getComponent(): CsatComponent {
        return DaggerCsatComponent
            .builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        fun newInstance(): CsatBottomsheet {
            val bottomSheet = CsatBottomsheet()
            val bundle = Bundle()
            // ... extra
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }

}
