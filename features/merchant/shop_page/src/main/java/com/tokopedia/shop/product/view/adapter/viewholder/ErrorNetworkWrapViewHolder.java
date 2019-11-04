package com.tokopedia.shop.product.view.adapter.viewholder;


import androidx.annotation.LayoutRes;
import android.view.View;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.shop.R;

public class ErrorNetworkWrapViewHolder extends ErrorNetworkViewHolder {
    @LayoutRes
    public final static int LAYOUT = R.layout.partial_empty_wrap_page_error;

    public ErrorNetworkWrapViewHolder(View itemView) {
        super(itemView);
    }
}
