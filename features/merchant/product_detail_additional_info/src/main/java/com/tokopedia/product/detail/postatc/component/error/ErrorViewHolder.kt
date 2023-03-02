package com.tokopedia.product.detail.postatc.component.error

import com.tokopedia.globalerror.GlobalError
import com.tokopedia.product.detail.databinding.ItemPostAtcErrorBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class ErrorViewHolder(
    private val binding: ItemPostAtcErrorBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<ErrorUiModel>(binding.root) {
    override fun bind(element: ErrorUiModel) {
        binding.apply {
            val errorType = element.errorType
            if(errorType == GlobalError.SERVER_ERROR){

            } else{
                postAtcError.setType(element.errorType)
                postAtcError.setActionClickListener {
                    listener.refreshPage()
                }
            }
        }
    }
}
