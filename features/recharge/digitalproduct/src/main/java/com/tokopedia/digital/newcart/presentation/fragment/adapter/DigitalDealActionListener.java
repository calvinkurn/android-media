package com.tokopedia.digital.newcart.presentation.fragment.adapter;

import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

public interface DigitalDealActionListener {
    void actionBuyButton(DealProductViewModel productViewModel);

    void actionCloseButon(DealProductViewModel productViewModel);

    void actionDetail(DealProductViewModel productViewModel);
}
