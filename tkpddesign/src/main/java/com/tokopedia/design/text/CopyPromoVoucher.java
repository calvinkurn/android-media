package com.tokopedia.design.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by nakama on 12/5/17.
 */

public class CopyPromoVoucher extends BaseCustomView {

    private TextView tvVoucherCode;
    private View buttonCopy;
    private View iconCopyCode;
    private View textCopyCode;
    private CountDownTimer timer;

    private String textVoucherCode;
    private CallbackListener callbackListener;

    public CopyPromoVoucher(@NonNull Context context) {
        super(context);
        init();
    }

    public CopyPromoVoucher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CopyPromoVoucher(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_copy_voucher_code, this);
        tvVoucherCode = (TextView) view.findViewById(R.id.text_voucher_code);
        iconCopyCode = view.findViewById(R.id.icon_copy_code);
        textCopyCode = view.findViewById(R.id.text_copy_code);
        buttonCopy = view.findViewById(R.id.button_copy);

        iconCopyCode.setVisibility(GONE);
        textCopyCode.setVisibility(VISIBLE);

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                flipBackground(false);
                tvVoucherCode.setText(textVoucherCode);
            }
        };

        buttonCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTextToClipBoard();
                flipBackground(true);
                tvVoucherCode.setText(getContext().getString(R.string.success_copy_voucher_code));
                timer.start();
                if (callbackListener != null) {
                    callbackListener.onCopyButtonClick();
                }
            }
        });
    }

    private void flipBackground(boolean active) {
        if (active) {
            iconCopyCode.setVisibility(VISIBLE);
            textCopyCode.setVisibility(INVISIBLE);
            buttonCopy.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_copy_voucher_code_right_active));
            tvVoucherCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_copy_voucher_code_left_active));
        } else {
            iconCopyCode.setVisibility(GONE);
            textCopyCode.setVisibility(VISIBLE);
            buttonCopy.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_copy_voucher_code_right_default));
            tvVoucherCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_copy_voucher_code_left_default));
        }
    }

    private void copyTextToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("voucher code", textVoucherCode);
        clipboard.setPrimaryClip(clip);
    }

    public void setTextVoucherCode(String textVoucherCode) {
        this.textVoucherCode = textVoucherCode;
        tvVoucherCode.setText(textVoucherCode);
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public interface CallbackListener {
        void onCopyButtonClick();
    }
}
