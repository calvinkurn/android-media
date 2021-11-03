package com.tokopedia.product.manage.feature.violation.view.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.manage.databinding.BottomSheetProductManageViolationBinding
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonAdapter
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonItemViewHolder
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ViolationReasonBottomSheet: BottomSheetUnify(), ViolationReasonItemViewHolder.Listener {

    companion object {
        fun createInstance(): ViolationReasonBottomSheet {
            return ViolationReasonBottomSheet()
        }

        private const val TAG = "ViolationReasonBottomSheet"
    }

    private var binding by autoClearedNullable<BottomSheetProductManageViolationBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageViolationBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onUrlClicked(url: String) {
        goToUrl(url)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupView() {
        // TODO: Remove dummy
        setTitle("Pelanggaran atribut")
        val dummyUiModel = ViolationReasonUiModel(
            "Pelanggaran atribut",
            "Produk diduga melanggar hukum karena:",
            "Berupa organ tubuh manusia",
            "Lakukan langkah berikut untuk menyelesaikan:",
            listOf(
                "Pelajari tentang <a href=\"https://www.tokopedia.com/login\">produk yang dilarang</a>",
                "Tambah produk sesuai <a href=\"https://www.tokopedia.com/login\">Syarat & Ketentuan</a> yang berlaku di Tokopedia",
            ),
            "Bantuan",
            "sellerapp://home"
        )
        binding?.run {
            tvProductManageViolationTitle.text = dummyUiModel.descTitle
            tvProductManageViolationReason.text = dummyUiModel.descReason
            tvProductManageViolationStepTitle.text = dummyUiModel.stepTitle
            btnProductManageViolationAction.run {
                text = dummyUiModel.buttonText
                setOnClickListener {
                    context?.let {
                        RouteManager.route(it, dummyUiModel.buttonApplink)
                    }
                }
            }

            context?.let {
                val adapter = ViolationReasonAdapter(it, dummyUiModel.stepList, this@ViolationReasonBottomSheet)
                rvProductManageViolationStep.layoutManager = LinearLayoutManager(it)
                rvProductManageViolationStep.adapter = adapter
            }
        }
    }

    private fun goToUrl(url: String) {
        Uri.parse(url).let { uri ->
            val myIntent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(myIntent)
        }
    }

}