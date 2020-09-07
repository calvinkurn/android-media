package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.REQUEST_CAPTURE_SAVING_BOOK
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.SAVING_BOOk
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneUploadDataActivity
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding_saving_book.*

@Deprecated("Deleted soon")
class InactivePhoneOnboardingSavingBookFragment : BaseDaggerFragment() {

    private val userSession: UserSession = UserSession(activity)

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding_saving_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface

        btnNext?.setOnClickListener {
            takePicture()
        }

        chipSavingBook?.setOnClickListener {
            itemLayoutSavingBook?.visibility = View.VISIBLE
            itemLayoutReceipt?.visibility = View.GONE
            chipSavingBook?.chipType = ChipsUnify.TYPE_SELECTED
            chipReceipt?.chipType = ChipsUnify.TYPE_NORMAL
        }

        chipReceipt?.setOnClickListener {
            itemLayoutSavingBook?.visibility = View.GONE
            itemLayoutReceipt?.visibility = View.VISIBLE
            chipSavingBook?.chipType = ChipsUnify.TYPE_NORMAL
            chipReceipt?.chipType = ChipsUnify.TYPE_SELECTED
        }
    }

    private fun takePicture() {
        val intent = InactivePhoneImagePickerActivity.createIntentCameraWithGallery(context, CameraViewMode.SAVING_BOOK)
        startActivityForResult(intent, REQUEST_CAPTURE_SAVING_BOOK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_SAVING_BOOK && resultCode == Activity.RESULT_OK) {
            gotoPageUploadData()
        }
    }

    private fun gotoPageUploadData() {
        activity?.let {
            startActivity(InactivePhoneUploadDataActivity.getIntent(it, SAVING_BOOk))
        }
    }
}