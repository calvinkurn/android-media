package com.tokopedia.recharge_credit_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.recharge_credit_card.widget.CCClientNumberWidget
import kotlinx.android.synthetic.main.fragment_recharge_cc.*

class RechargeCCFragment : BaseDaggerFragment() {

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_cc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cc_widget_client_number.setListener(object : CCClientNumberWidget.ActionListener {
            override fun onClickNextButton(clientNumber: String) {
                dialogConfirmation()
            }
        })
    }

    private fun dialogConfirmation() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.cc_title_dialog))
            dialog.setDescription(getString(R.string.cc_desc_dialog))
            dialog.setPrimaryCTAText(getString(R.string.cc_cta_btn_primary))
            dialog.setSecondaryCTAText(getString(R.string.cc_cta_btn_secondary))
            dialog.setPrimaryCTAClickListener {
                Toast.makeText(it, "Lanjut", Toast.LENGTH_SHORT).show()
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    companion object {

        fun newInstance(): Fragment {
            return RechargeCCFragment()
        }
    }
}