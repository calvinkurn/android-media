package com.tokopedia.notifications.inApp.viewEngine;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMBackground;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMText;

import java.util.List;

/**
 * @author lalit.singh
 */
public class ViewEngine {

    private Activity mActivity;

    private Context appContext;

    private static ViewEngine instance;

    private static final Object lock = new Object();


    private int resCmClose = R.id.iv_close;
    private int resCmImage = R.id.iv_cmImage;
    private int resCmTitle = R.id.tv_cmTitle;
    private int resCmMessage = R.id.tv_cmMessage;
    private int buttonContainer = R.id.ll_buttonContainer;

    public static ViewEngine getInstance(Context context) {
        synchronized (lock) {
            if (instance == null)
                instance = new ViewEngine(context.getApplicationContext());
        }
        return instance;
    }

    public boolean isViewAlreadyAttached(Activity activity) {
        return false;
    }

    private ViewEngine(Context context) {
        this.appContext = context;
    }

    public CMInApp createView(Activity activity, CMInApp cmInApp) {
        try {
            if (activity == null)
                return null;
            mActivity = activity;
            View view = LayoutInflater.from(appContext).inflate(R.layout.layout_inapp, null, false);
            view.findViewById(R.id.mainContainer).setOnClickListener(v -> {
            });
            view.findViewById(R.id.mainContainer).setTag(cmInApp);
            cmInApp.setCmInAppView(view);
            View innerContainer = view.findViewById(R.id.innerContainer);
            addOnClose(view);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) innerContainer.getLayoutParams();
            setMainContainerMargin(layoutParams, cmInApp, innerContainer);
            setMainContainerBackGround(view, cmInApp);
            CMLayout cmLayout = cmInApp.getCmLayout();

            if (!cmInApp.isCancelable()) {
                innerContainer.findViewById(R.id.iv_close).setVisibility(View.INVISIBLE);
            }

            if (cmInApp.getType().equalsIgnoreCase(CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY) ||
                    cmInApp.getType().equalsIgnoreCase(CmInAppConstant.TYPE_FULL_SCREEN_IMAGE_ONLY)) {
                innerContainer.findViewById(R.id.tv_cmMessage).setVisibility(View.GONE);
                innerContainer.findViewById(R.id.tv_cmTitle).setVisibility(View.GONE);
                innerContainer.findViewById(R.id.ll_buttonContainer).setVisibility(View.GONE);
            } else {
                setCmInAppTitle(view, cmLayout);
                setCmInAppMessage(view, cmLayout);
                setButtons(view, cmInApp);
            }

            setDrawable(innerContainer, cmLayout.getForeground());
            setCmInAppImage(view, cmLayout, (ConstraintLayout) innerContainer);
            handleBackPress(view);
            return cmInApp;
        } catch (Exception e) {
            Log.d("ViewEngine", e.getMessage());
            return null;
        }
    }

    private void setMainContainerMargin(RelativeLayout.LayoutParams layoutParams, CMInApp cmInApp, View innerContainer) {
        int navigationHeight = getNavigationBarHeight();
        int statusBarHeight = getStatusBarHeight();
        int[] margins = {0, statusBarHeight, 0, navigationHeight};
        RelativeLayout.LayoutParams innerLayoutParams = (RelativeLayout.LayoutParams) innerContainer.getLayoutParams();

        switch (cmInApp.getType()) {
            case CmInAppConstant.TYPE_FULL_SCREEN:
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                innerLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                break;

            case CmInAppConstant.TYPE_INTERSTITIAL:
                innerLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                innerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                margins[0] = margins[0] + (int) getPXtoDP(40F);
                margins[1] = margins[1] + (int) getPXtoDP(40F);
                margins[2] = margins[2] + (int) getPXtoDP(40F);
                margins[3] = margins[3] + (int) getPXtoDP(40F);
                break;
            case CmInAppConstant.TYPE_BORDER_BOTTOM:
                innerLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                innerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                changeConstraintToBorderView((ConstraintLayout) innerContainer, cmInApp);
                break;
            case CmInAppConstant.TYPE_BORDER_TOP:
                innerLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                innerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                changeConstraintToBorderView((ConstraintLayout) innerContainer, cmInApp);
                break;
            case CmInAppConstant.TYPE_ALERT:
                innerLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                innerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                changeConstraintToBorderView((ConstraintLayout) innerContainer, cmInApp);
                margins[0] = margins[0] + (int) getPXtoDP(40F);
                margins[1] = margins[1] + (int) getPXtoDP(40F);
                margins[2] = margins[2] + (int) getPXtoDP(40F);
                margins[3] = margins[3] + (int) getPXtoDP(40F);
                break;

            case CmInAppConstant.TYPE_FULL_SCREEN_IMAGE_ONLY:
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                innerLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                innerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                changeToImageOnly((ConstraintLayout) innerContainer);
                if (cmInApp.getCmLayout().getAppLink() != null)
                    addOnClickAction(cmInApp, innerContainer, cmInApp.getCmLayout().getAppLink());
                break;

            case CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY:
                innerLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                innerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                changeToImageOnly((ConstraintLayout) innerContainer);
                margins[0] = margins[0] + (int) getPXtoDP(40F);
                margins[1] = margins[1] + (int) getPXtoDP(40F);
                margins[2] = margins[2] + (int) getPXtoDP(40F);
                margins[3] = margins[3] + (int) getPXtoDP(40F);
                if (cmInApp.getCmLayout().getAppLink() != null)
                    addOnClickAction(cmInApp, innerContainer, cmInApp.getCmLayout().getAppLink());
                break;
        }

        innerContainer.setLayoutParams(innerLayoutParams);
        layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
    }

    private void setMainContainerBackGround(View view, CMInApp cmInApp) {
        if (!cmInApp.getType().equalsIgnoreCase(CmInAppConstant.TYPE_FULL_SCREEN))
            view.setBackgroundColor(Color
                    .parseColor("#88000000"));
    }

    private void setCmInAppImage(View view, CMLayout cmLayout, ConstraintLayout constraintLayout) {
        ImageView imageView = view.findViewById(resCmImage);
        String imageUrlStr = cmLayout.getImg();
        if (!TextUtils.isEmpty(imageUrlStr)) {
            Glide.with(mActivity)
                    .load(imageUrlStr)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private void setCmInAppTitle(View view, CMLayout cmLayout) {
        TextView titleTextView = view.findViewById(R.id.tv_cmTitle);
        CMText titleCmText = cmLayout.getTitleText();
        if (titleCmText == null) {
            titleTextView.setVisibility(View.INVISIBLE);
        } else {
            titleTextView.setText(getSpannedTextFromStr(titleCmText.getTxt()));
            titleTextView.setTextColor(Color.parseColor(titleCmText.getColor()));
            if (!TextUtils.isEmpty(titleCmText.getSize()))
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        Float.parseFloat(titleCmText.getSize()));
        }
    }

    private void setCmInAppMessage(View view, CMLayout cmLayout) {
        TextView titleTextView = view.findViewById(R.id.tv_cmMessage);
        CMText messageCmText = cmLayout.getMessageText();
        if (messageCmText == null) {
            titleTextView.setVisibility(View.INVISIBLE);
        } else {
            titleTextView.setText(getSpannedTextFromStr(messageCmText.getTxt()));
            titleTextView.setTextColor(Color.parseColor(messageCmText.getColor()));
            if (!TextUtils.isEmpty(messageCmText.getSize()))
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        Float.parseFloat(messageCmText.getSize()));
        }
    }

    private void setButtons(View mainContainer, CMInApp cmInApp) {
        CMLayout cmLayout = cmInApp.getCmLayout();
        String buttonOrientation = cmLayout.getBtnOrientation();
        List<CMButton> cmButtonList = cmLayout.getButton();
        if (cmButtonList == null) {
            return;
        }
        LinearLayout container = mainContainer.findViewById(R.id.ll_buttonContainer);

        int buttonCount = cmButtonList.size();
        if (buttonCount == 0) {
            mainContainer.findViewById(R.id.ll_buttonContainer).setVisibility(View.GONE);
            return;
        }

        if (!TextUtils.isEmpty(buttonOrientation) && buttonOrientation.equalsIgnoreCase(CmInAppConstant.ORIENTATION_HORIZONTAL)) {
            container.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            container.setOrientation(LinearLayout.VERTICAL);
        }
        for (CMButton cmButton : cmButtonList) {
            buttonCount--;
            Button button = createButton(container, cmLayout.getBtnOrientation(), cmButton, buttonCount == 0);
            if (cmButton.getAppLink() != null)
                addOnClickAction(cmInApp, button, cmButton.getAppLink());
        }
    }

    private Button createButton(LinearLayout buttonContainer, String orientation, CMButton cmButton, boolean isLastButton) {
        Button button = new Button(appContext);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setText(cmButton.getTxt());
        button.setTextColor(Color.parseColor(cmButton.getColor()));

        int margin[] = {0, 0, 0, 0};
        switch (orientation) {
            case CmInAppConstant.ORIENTATION_VERTICAL:
                margin[1] = (int) getPXtoDP(8);
                break;
            case CmInAppConstant.ORIENTATION_HORIZONTAL:
                layoutParams.weight = 1;
                if (!isLastButton)
                    margin[2] = (int) getPXtoDP(8);
                break;
        }

        layoutParams.setMargins(margin[0], margin[1], margin[2], margin[3]);

        int padding = (int) getPXtoDP(5);

        if (cmButton.getPadding() != 0)
            padding = cmButton.getPadding();

        button.setLayoutParams(layoutParams);

        button.setPadding(padding, padding, padding, padding);
        buttonContainer.addView(button);

        CMBackground background = new CMBackground();
        background.setColor(cmButton.getBgColor());
        background.setCornerRadius(cmButton.getCornerRadius());
        background.setStrokeColor(cmButton.getStrokeColor());
        background.setStrokeWidth(cmButton.getStrokeWidth());

        setDrawable(button, background);
        return button;
    }

    private void addOnClickAction(final CMInApp cmInApp, View actionableView, String deepLink) {
        if (deepLink.equalsIgnoreCase("close")) {
            addOnClose(actionableView);
            return;
        }
        actionableView.setTag(deepLink);
        actionableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deepLink = v.getTag().toString();
                CmInAppListener listener = CMInAppManager.getCmInAppListener();
                if (listener != null && !TextUtils.isEmpty(deepLink)) {
                    listener.onCMinAppDismiss();
                    listener.onCMInAppLinkClick(Uri.parse(deepLink), cmInApp.getScreen());
                }
                ((ViewGroup) cmInApp.getCmInAppView().getParent()).removeView(cmInApp.getCmInAppView());
            }
        });
    }

    private int getNavigationBarHeight() {
        Resources resources = appContext.getResources();
        if (!hasSoftKeys(mActivity.getWindowManager()))
            return 0;
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean hasSoftKeys(WindowManager windowManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            return false;
        }
    }

    private int getStatusBarHeight() {
        return (int) (24 * appContext.getResources().getDisplayMetrics().density);
    }

    private void addOnClose(final View view) {
        View closeView = view.findViewById(R.id.iv_close);
        closeView.setOnClickListener(v -> {
            CmInAppListener listener = CMInAppManager.getCmInAppListener();
            if (listener != null) {
                listener.onCMInAppClosed();
                listener.onCMinAppDismiss();
            }
            ((ViewGroup) view.getParent()).removeView(view);
        });

    }

    private void handleBackPress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    CmInAppListener listener = CMInAppManager.getCmInAppListener();
                    if (listener != null) {
                        listener.onCMInAppClosed();
                        listener.onCMinAppDismiss();
                    }
                    ((ViewGroup) v.getParent()).removeView(v);
                    return true;
                }
            }
            return false;
        });
    }

    private void setDrawable(View view, CMBackground cmBackground) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(cmBackground.getColor()));
        gd.setShape(GradientDrawable.RECTANGLE);
        if (cmBackground.getCornerRadius() > 0)
            gd.setCornerRadius(cmBackground.getCornerRadius());
        if (!TextUtils.isEmpty(cmBackground.getStrokeColor()))
            gd.setStroke(cmBackground.getStrokeWidth(),
                    Color.parseColor(cmBackground.getStrokeColor()));
        gd.setSize(view.getWidth(), view.getHeight());
        view.setBackground(gd);
    }

    private float getPXtoDP(float dip) {
        return dip * ((float) appContext.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);


    }

    private void changeConstraintToBorderView(ConstraintLayout constraintLayout, CMInApp cmInApp) {
        boolean isCLoseButtonVisible = false;
        if (cmInApp.isCancelable()) {
            isCLoseButtonVisible = true;
        }else {
            constraintLayout.findViewById(resCmClose).setVisibility(View.GONE);
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) constraintLayout
                .findViewById(resCmImage).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        constraintLayout.findViewById(resCmImage).setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) constraintLayout
                .findViewById(resCmTitle).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        constraintLayout.findViewById(resCmTitle).setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) constraintLayout
                .findViewById(resCmMessage).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        constraintLayout.findViewById(resCmMessage).setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) constraintLayout
                .findViewById(buttonContainer).getLayoutParams();
        params.setMargins(0, 0, 0, 0);

        constraintLayout.findViewById(buttonContainer).setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) constraintLayout
                .findViewById(resCmClose).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        constraintLayout.findViewById(resCmClose).setLayoutParams(params);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.clear(resCmTitle);
        constraintSet.clear(resCmImage);
        constraintSet.clear(resCmMessage);

        if (!TextUtils.isEmpty(cmInApp.cmLayout.img)) {
            constraintSet.constrainHeight(resCmImage, (int) getPXtoDP(80));
            constraintSet.constrainWidth(resCmImage, (int) getPXtoDP(80));
            if (isCLoseButtonVisible)
                constraintSet.connect(resCmImage, ConstraintSet.TOP,
                        resCmClose, ConstraintSet.BOTTOM);
            else
                constraintSet.connect(resCmImage, ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.connect(resCmImage, ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
        } else {
            constraintLayout.findViewById(resCmImage).setVisibility(View.GONE);
        }

        /*title*/
        constraintSet.constrainWidth(R.id.tv_cmTitle, ConstraintSet.WRAP_CONTENT);

        if (isCLoseButtonVisible)
            constraintSet.connect(resCmTitle, ConstraintSet.TOP,
                    resCmClose, ConstraintSet.BOTTOM);
        else
            constraintSet.connect(resCmTitle, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP);

        constraintSet.connect(resCmTitle, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END);

        /*message*/
        constraintSet.constrainWidth(resCmMessage, ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(resCmMessage, 0);
        constraintSet.connect(resCmMessage, ConstraintSet.TOP,
                resCmTitle, ConstraintSet.BOTTOM);
        constraintSet.connect(resCmMessage, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END);

        /*button Container*/
        constraintSet.connect(buttonContainer, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(buttonContainer, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(buttonContainer, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END);


        if (TextUtils.isEmpty(cmInApp.cmLayout.img)) {

            /*title*/
            constraintSet.connect(resCmTitle, ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);

            /*message*/
            constraintSet.connect(resCmMessage, ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);

            /*button container*/
            constraintSet.connect(buttonContainer, ConstraintSet.TOP,
                    resCmMessage, ConstraintSet.BOTTOM);


            constraintSet.setMargin(resCmTitle, ConstraintSet.START, 0);
            constraintSet.setMargin(resCmMessage, ConstraintSet.START, 0);
            constraintSet.setMargin(buttonContainer, ConstraintSet.TOP, (int) getPXtoDP(10));

        } else {

            /*title*/
            constraintSet.connect(resCmTitle, ConstraintSet.START,
                    resCmImage, ConstraintSet.END);
            /*message*/
            constraintSet.connect(resCmMessage, ConstraintSet.START,
                    resCmImage, ConstraintSet.END);
            /*button container*/
            constraintSet.connect(buttonContainer, ConstraintSet.TOP,
                    resCmImage, ConstraintSet.BOTTOM);

            constraintSet.setMargin(resCmTitle, ConstraintSet.START, (int) getPXtoDP(8));
            constraintSet.setMargin(resCmMessage, ConstraintSet.START, (int) getPXtoDP(8));
            constraintSet.setMargin(buttonContainer, ConstraintSet.TOP, (int) getPXtoDP(8));

        }


        constraintSet.setHorizontalBias(resCmTitle, 0);
        constraintSet.setHorizontalBias(resCmMessage, 0);
        constraintSet.setVerticalBias(resCmMessage, 0);

        constraintLayout.setPadding((int) getPXtoDP(16),
                (int) getPXtoDP(16),
                (int) getPXtoDP(16),
                (int) getPXtoDP(16));


        constraintSet.setMargin(resCmMessage, ConstraintSet.TOP, (int) getPXtoDP(3));

        constraintSet.applyTo(constraintLayout);

        ((TextView) constraintLayout.findViewById(resCmTitle)).setGravity(Gravity.START);
        ((TextView) constraintLayout.findViewById(resCmMessage)).setGravity(Gravity.START);

    }

    private void changeToImageOnly(ConstraintLayout constraintLayout) {
        constraintLayout.setPadding(0, 0, 0, 0);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.constrainHeight(R.id.iv_cmImage, 0);
        constraintSet.constrainWidth(R.id.iv_cmImage, 0);

        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 0);

        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.connect(R.id.iv_cmImage, ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);


        ((ConstraintLayout.LayoutParams) (constraintLayout.findViewById(R.id.iv_cmImage)
                .getLayoutParams()))
                .setMargins(0, 0, 0, 0);

        constraintSet.applyTo(constraintLayout);

        setImageAspectRatio(constraintLayout, constraintLayout.findViewById(R.id.iv_cmImage), "2:1");

    }

    private static Spanned getSpannedTextFromStr(String str) {
        if (null == str)
            return new SpannableStringBuilder("");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(str);
            }
        } catch (Exception e) {
            return new SpannableStringBuilder(str);
        }
    }

    private void setImageAspectRatio(ConstraintLayout mLayout, View mView, String imageRatio) {
        ConstraintSet set = new ConstraintSet();
        set.clone(mLayout);
        //set.setDimensionRatio(mView.getId(), imageRatio);
        set.setDimensionRatio(mView.getId(), null);
        set.applyTo(mLayout);
    }
}
