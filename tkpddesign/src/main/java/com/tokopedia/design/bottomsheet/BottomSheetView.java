package com.tokopedia.design.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.design.R;

/**
 * Created by nabillasabbaha on 9/6/17.
 */

public class BottomSheetView extends BottomSheetDialog {

    protected TextView titleBottomSheet;
    protected TextView bodyBottomSheet;
    protected ImageView imgIconBottomSheet;
    protected TextView linkBottomSheet;
    protected TextView btnCloseBottomSheet;
    protected TextView btnOpsiBottomSheet;
    protected Context context;
    protected View bottomSheetView;
    protected ActionListener listener;
    protected LayoutInflater layoutInflater;

    public BottomSheetView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BottomSheetView(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init(context);
    }

    public BottomSheetView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    protected void init(Context context) {
        this.context = context;
        layoutInflater = ((Activity) context).getLayoutInflater();
        bottomSheetView = layoutInflater.inflate(getLayout(), null);
        setContentView(bottomSheetView);

        titleBottomSheet = (TextView) findViewById(R.id.title_bottom_sheet);
        bodyBottomSheet = (TextView) findViewById(R.id.body_bottom_sheet);
        imgIconBottomSheet = (ImageView) findViewById(R.id.img_bottom_sheet);
        linkBottomSheet = (TextView) findViewById(R.id.link_bottom_sheet);
        btnCloseBottomSheet = (TextView) findViewById(R.id.button_close_bottom_sheet);
        btnOpsiBottomSheet = (TextView) findViewById(R.id.button_opsi_bottom_sheet);

        addListener();
    }

    protected int getLayout() {
        return R.layout.widget_bottom_sheet;
    }

    public void addListener() {
        btnCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void renderBottomSheet(BottomSheetField bottomSheetField) {
        if (bottomSheetField == null) {
            return;
        }

        if (imgIconBottomSheet != null) {
            if (bottomSheetField.getImg() <= 0) {
                imgIconBottomSheet.setVisibility(View.GONE);
            } else {
                imgIconBottomSheet.setVisibility(View.VISIBLE);
                imgIconBottomSheet.setImageDrawable(ContextCompat.getDrawable(context, bottomSheetField.getImg()));
            }
        }


        titleBottomSheet.setText(bottomSheetField.getTitle());

        if (bodyBottomSheet != null)
            bodyBottomSheet.setText(bottomSheetField.getBody());

        if (linkBottomSheet != null) {
            if (bottomSheetField.getUrlTextLink() != null) {
                linkBottomSheet.setVisibility(View.VISIBLE);
                linkBottomSheet.setText(bottomSheetField.getLabelTextLink());
                linkBottomSheet.setOnClickListener(getClickTextLinkListener(bottomSheetField.getUrlTextLink()));
            } else {
                linkBottomSheet.setVisibility(View.GONE);
            }
        }

        if (bottomSheetField.getUrlButton() != null) {
            btnOpsiBottomSheet.setVisibility(View.VISIBLE);
            btnOpsiBottomSheet.setText(bottomSheetField.getLabelButton());
            btnOpsiBottomSheet.setOnClickListener(getClickButtonListener(
                    bottomSheetField.getUrlButton(), bottomSheetField.getAppLinkButton())
            );
        } else {
            btnOpsiBottomSheet.setVisibility(View.GONE);
        }

        if(bottomSheetField.getLabelCloseButton() != null){
            btnCloseBottomSheet.setText(bottomSheetField.getLabelCloseButton());
        }
    }

    public void setBtnCloseOnClick(View.OnClickListener onClickListener){
        btnCloseBottomSheet.setOnClickListener(onClickListener);
    }

    public void setBtnOpsiOnClick(View.OnClickListener onClickListener){
        btnOpsiBottomSheet.setOnClickListener(onClickListener);
    }

    protected View.OnClickListener getClickTextLinkListener(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickOnTextLink(url);
            }
        };
    }

    protected View.OnClickListener getClickButtonListener(final String url, final String appLink) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickOnButton(url, appLink);
                dismiss();
            }
        };
    }

    public void setTitleTextSize(float dimension) {
        titleBottomSheet.setTextSize(TypedValue.COMPLEX_UNIT_PX,dimension);
    }

    public void setBodyTextSize(float dimension) {
        bodyBottomSheet.setTextSize(TypedValue.COMPLEX_UNIT_PX,dimension);
    }

    public static class BottomSheetField {

        private final String title;
        private final String body;
        private final int img;
        private final String urlTextLink;
        private final String urlButton;
        private final String labelButton;
        private final String labelCloseButton;
        private final String labelTextLink;
        private final String appLinkButton;

        private BottomSheetField(BottomSheetFieldBuilder builder) {
            this.title = builder.title;
            this.body = builder.body;
            this.img = builder.img;
            this.appLinkButton = builder.appLinkButton;
            this.urlTextLink = builder.urlTextLink;
            this.urlButton = builder.urlButton;
            this.labelTextLink = builder.labelTextLink;
            this.labelButton = builder.labelButton;
            this.labelCloseButton = builder.labelCloseButton;

        }

        public String getLabelCloseButton() {
            return labelCloseButton;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public int getImg() {
            return img;
        }

        public String getUrlTextLink() {
            return urlTextLink;
        }

        public String getUrlButton() {
            return urlButton;
        }

        public String getAppLinkButton() {
            return appLinkButton;
        }

        public String getLabelButton() {
            return labelButton;
        }

        public String getLabelTextLink() {
            return labelTextLink;
        }

        public static class BottomSheetFieldBuilder {

            private String title;
            private String body;
            private int img;
            private String urlTextLink;
            private String urlButton;
            private String appLinkButton;
            private String labelTextLink;
            private String labelButton;
            private String labelCloseButton;

            public BottomSheetFieldBuilder setTitle(String title) {
                this.title = title;
                return this;
            }

            public BottomSheetFieldBuilder setBody(String body) {
                this.body = body;
                return this;
            }

            public BottomSheetFieldBuilder setImg(int img) {
                this.img = img;
                return this;
            }


            public BottomSheetFieldBuilder setUrlButton(String url, String appLink, String labelButton) {
                this.urlButton = url;
                this.appLinkButton = appLink;
                this.labelButton = labelButton;
                return this;
            }

            public BottomSheetFieldBuilder setUrlButton(String url, String labelButton) {
                this.urlButton = url;
                this.appLinkButton = "";
                this.labelButton = labelButton;
                return this;
            }

            public BottomSheetFieldBuilder setCloseButton(String labelCloseButton){
                this.labelCloseButton= labelCloseButton;
                return this;
            }

            public BottomSheetField build() {
                return new BottomSheetField(this);
            }
        }
    }

    public interface ActionListener {
        void clickOnTextLink(String url);

        void clickOnButton(String url, String appLink);
    }
}