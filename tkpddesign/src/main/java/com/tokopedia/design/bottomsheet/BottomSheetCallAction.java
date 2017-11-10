package com.tokopedia.design.bottomsheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * @author anggaprasetiyo on 10/12/17.
 */

public class BottomSheetCallAction extends BottomSheetDialog {
    private static final String DEFAULT_LABEL_ACTION_1 = "Call";
    private static final String DEFAULT_LABEL_ACTION_2 = "Message";
    private static final String DEFAULT_LABEL_TITLE = "Hubungi";
    private static final int DEFAULT_ICON_ACTION_1 = R.drawable.ic_phone_black_36dp;
    private static final int DEFAULT_ICON_ACTION_2 = R.drawable.ic_textsms_action;

    private CallActionData callActionData;
    private ActionListener actionListener;
    private boolean dismissActionClicked;

    public BottomSheetCallAction(@NonNull Context context) {
        super(context);
        initialView(context);
    }

    public BottomSheetCallAction(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initialView(context);
    }

    protected BottomSheetCallAction(@NonNull Context context, boolean cancelable,
                                    OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialView(context);
    }

    public void setCallActionData(CallActionData callActionData) {
        this.callActionData = callActionData;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private BottomSheetCallAction(Builder builder, Context context) {
        super(context);
        callActionData = builder.callActionData;
        actionListener = builder.actionListener;
        dismissActionClicked = builder.dismissActionClicked;
        initialView(context);
    }

    private void initialView(Context context) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(
                R.layout.widget_bottom_sheet_dialog_logistic_caller, null
        );
        setContentView(view);
        View btnAction1 = view.findViewById(R.id.btn_action_1);
        View btnAction2 = view.findViewById(R.id.btn_action_2);
        TextView tvLabel = (TextView) view.findViewById(R.id.tv_title);
        TextView tvLabelAction1 = (TextView) view.findViewById(R.id.tv_label_action_1);
        TextView tvLabelAction2 = (TextView) view.findViewById(R.id.tv_label_action_2);
        ImageView ivAction1 = (ImageView) view.findViewById(R.id.iv_icon_action_1);
        ImageView ivAction2 = (ImageView) view.findViewById(R.id.iv_icon_action_2);

        tvLabel.setText(
                callActionData.getLabelTitle() != null ?
                        callActionData.getLabelTitle() : DEFAULT_LABEL_TITLE
        );
        tvLabelAction1.setText(
                callActionData.getLabelAction1() != null ?
                        callActionData.getLabelAction1() : DEFAULT_LABEL_ACTION_1
        );
        tvLabelAction2.setText(
                callActionData.getLabelAction2() != null ?
                        callActionData.getLabelAction2() : DEFAULT_LABEL_ACTION_2
        );

        ivAction1.setImageResource(
                callActionData.getIconAction1() != 0 ?
                        callActionData.getIconAction1() : DEFAULT_ICON_ACTION_1
        );
        ivAction2.setImageResource(
                callActionData.getIconAction2() != 0 ?
                        callActionData.getIconAction2() : DEFAULT_ICON_ACTION_2
        );

        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onCallAction1Clicked(callActionData);
                if (dismissActionClicked && isShowing()) dismiss();
            }
        });
        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onCallAction2Clicked(callActionData);
                if (dismissActionClicked && isShowing()) dismiss();
            }
        });
    }

    public static class CallActionData {
        private String phoneNumber;
        private String labelTitle;
        private String labelAction1;
        private String labelAction2;
        private int iconAction1;
        private int iconAction2;

        public CallActionData setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CallActionData setLabelTitle(String labelTitle) {
            this.labelTitle = labelTitle;
            return this;
        }

        public CallActionData setLabelAction1(String labelAction1) {
            this.labelAction1 = labelAction1;
            return this;
        }

        public CallActionData setLabelAction2(String labelAction2) {
            this.labelAction2 = labelAction2;
            return this;
        }

        public CallActionData setIconAction1(int iconAction1) {
            this.iconAction1 = iconAction1;
            return this;
        }

        public CallActionData setIconAction2(int iconAction2) {
            this.iconAction2 = iconAction2;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        String getLabelTitle() {
            return labelTitle;
        }

        String getLabelAction1() {
            return labelAction1;
        }

        String getLabelAction2() {
            return labelAction2;
        }

        int getIconAction1() {
            return iconAction1;
        }

        int getIconAction2() {
            return iconAction2;
        }
    }

    public interface ActionListener {
        void onCallAction1Clicked(CallActionData callActionData);

        void onCallAction2Clicked(CallActionData callActionData);
    }


    public static final class Builder {
        private Context context;
        private CallActionData callActionData;
        private ActionListener actionListener;
        private boolean dismissActionClicked = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder callActionData(CallActionData val) {
            callActionData = val;
            return this;
        }

        public Builder actionListener(ActionListener val) {
            actionListener = val;
            return this;
        }

        public Builder dismissActionClicked(boolean val) {
            dismissActionClicked = val;
            return this;
        }

        public BottomSheetCallAction build() {
            return new BottomSheetCallAction(this, context);
        }
    }
}
