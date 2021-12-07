package com.tokopedia.product_ar.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.data.MFEMakeupRenderingParameters
import com.modiface.mfemakeupkit.widgets.MFEBeforeAfterMakeupView
import com.tokopedia.product_ar.R

class ProductArFragment : Fragment() {

    private var mMakeupView: MFEBeforeAfterMakeupView? = null
    private var mMakeupEngine: MFEMakeupEngine? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_ar, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMakeupEngine = MFEMakeupEngine(requireActivity().applicationContext, MFEMakeupEngine.Region.US)
        mMakeupEngine?.setMakeupRenderingParameters(MFEMakeupRenderingParameters(false))
        mMakeupEngine?.loadResources(requireActivity().applicationContext, null)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMakeupView = view.findViewById(R.id.main_img)


        mMakeupView?.setup(mMakeupEngine);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 123);
        } else {
            mMakeupEngine?.startRunningWithCamera(context)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMakeupEngine?.startRunningWithCamera(context)
            } else {
                AlertDialog.Builder(requireContext())
                        .setTitle("Permission Error")
                        .setMessage("The camera permission is needed for live mode. You must click allow to start the live try-on")
                        .show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductArFragment()
    }
}