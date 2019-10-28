package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tokopedia.digital.R;

/**
 * Created by Rizky on 16/05/18.
 */
public class NFCDisabledView extends LinearLayout {

    private OnActivateNFCClickListener listener;

    public interface OnActivateNFCClickListener {
        void onClick();
    }

    public NFCDisabledView(Context context) {
        super(context);
        init();
    }

    public NFCDisabledView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NFCDisabledView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnActivateNFCClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_nfc_disabled, this);

        Button buttonActivateNFC = view.findViewById(R.id.button_activate_nfc);

        buttonActivateNFC.setOnClickListener(v -> listener.onClick());
    }

}