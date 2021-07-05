package com.tokopedia.buyerorder.detail.view.customview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.buyerorder.R;
import com.tokopedia.unifycomponents.Toaster;

import static android.content.Context.CLIPBOARD_SERVICE;

public class BookingCodeView extends RelativeLayout {

    private static final String TEXT_COPIED = "Kode booking telah disalin";
    private static final String TEXT = "text";
    private Context context;
    private String bookingCode;
    private int position, totalItems;
    private String bookingCodeTitle;
    private TextView bookingCodeText, copyBookingCode, bookingCodeTitleView;
    private ImageView dividerBookingCode;
    private boolean isQRCodeAvailable;

    public BookingCodeView(Context context) {
        super(context);
        initView();
    }

    public BookingCodeView (Context context, String  bookingCode, int position, String bookingCodeTitle, int totalItems) {
        super(context);
        this.context = context;
        this.bookingCode = bookingCode;
        this.position = position;
        this.bookingCodeTitle = bookingCodeTitle;
        this.totalItems = totalItems;
        initView();
    }

    public BookingCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BookingCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_code_layout, this);
        bookingCodeText = view.findViewById(R.id.booking_code);
        bookingCodeTitleView = view.findViewById(R.id.booking_code_title);
        copyBookingCode = view.findViewById(R.id.copyCode);
        dividerBookingCode = view.findViewById(R.id.divider_booking_code);
        bookingCodeTitleView.setText(bookingCodeTitle);
        bookingCodeText.setText(bookingCode);
        copyBookingCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData myClip;
                ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText(TEXT, bookingCode);
                myClipboard.setPrimaryClip(myClip);
                Toaster.build(view, TEXT_COPIED, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, "Ok", v1 -> {
                }).show();

            }
        });

        if (position == totalItems - 1) {
            dividerBookingCode.setVisibility(GONE);
        }
    }
}
