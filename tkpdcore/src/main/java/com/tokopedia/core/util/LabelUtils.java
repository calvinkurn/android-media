package com.tokopedia.core.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.tokopedia.core.R;


/**
 * Created by Kris on 2/23/2015.
 */
public class LabelUtils {

    public static final int DEFAULT_MAX_TEXT_LENGTH = 21;

    private Context context;
    private TextView userName;
    private float labelWidth;
    private boolean detailView;
    int textLength = DEFAULT_MAX_TEXT_LENGTH;

    public static LabelUtils getInstance(Context context, TextView userName){
        LabelUtils privilege = new LabelUtils();
        privilege.userName = userName;
        privilege.context = context;
        return privilege;
    }

    public static LabelUtils getInstance(Context context, TextView userName, int textLength){
        LabelUtils privilege = new LabelUtils();
        privilege.userName = userName;
        privilege.context = context;
        privilege.textLength = textLength;
        return privilege;
    }
    public void giveLabel(String userRole){
        detailView = true;
        userNameTextViewLabelling(userRole);

    }
    public void giveSquareLabel(String userRole){
        detailView = false;
        userNameTextViewLabelling(userRole);
    }

    private String userNameFilter(String userNameString, String userRole){
        if(userNameString.equals("CustomerWrapper Service Tokopedia"))
            userNameString = "CS Tokopedia";
        userName.setEllipsize(null);
        if(userNameString.length()>maximumString(userRole) && !detailView){
            userNameString = userNameString.substring(0, maximumString(userRole)) + "...";
        }else if(userNameString.length()>maximumString(userRole) && detailView){
            userNameString = userNameString.substring(0, maximumString(userRole)) + "...";
        }
        return userNameString;
    }

    public class RoundedBackgroundSpan extends ReplacementSpan
    {
        private String roleLabelText;

        public RoundedBackgroundSpan(String roleString) {
            roleLabelText = roleString;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            return Math.round(labelWidth);
        }


        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
        {
            RectF rect = new RectF(x, top + 5 , x + labelWidth, bottom);
            paint.setColor((userPrivilegeSelection(roleLabelText)));
            canvas.drawRoundRect(rect, 3, 3, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(text, start, end, x, y, paint);
        }
    }
    private String userRoleWithGap(String userRole){
        if(detailView)
            return " " + userRole + "  ";
        else
            return " " + userRole + " ";
    }
    private boolean checkTextTypeFace(){
        if(userName.getTypeface()!=null&&userName.getTypeface().getStyle()==Typeface.BOLD){
            userName.setTypeface(null, Typeface.NORMAL);
            return true;
        }
        else
            return false;
    }
    private void reboldText(boolean textIsBold,Spannable wordToSpan, int roleLength, int userLength){
        if(textIsBold){
            roleLength = roleLength+1;
            userLength = userLength + roleLength;
            final StyleSpan bold = new StyleSpan(Typeface.BOLD);
            wordToSpan.setSpan(bold, roleLength , userLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
    private void userNameTextViewLabelling(String userRole){
        String user = userName.getText().toString();
        user = userNameFilter(user, userRole);
        String role = userRoleWithGap(userRole);
        labelWidth = userName.getPaint().measureText(role) * 0.7f;
        Spannable wordToSpan= new SpannableString(role + " " + user);
        wordToSpan.setSpan(new RelativeSizeSpan(0.7f), 0, role.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 0, role.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reboldText(checkTextTypeFace(), wordToSpan, role.length(), user.length());
        wordToSpan.setSpan(new RoundedBackgroundSpan(userRole), 0, role.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(!userRole.equals(""))
            userName.setText(wordToSpan);
    }
    private int maximumString(String userRole){
        if(userRole.length()>8)
            textLength = textLength-4;
        if(userName.getTypeface()!=null && userName.getTypeface().getStyle()==Typeface.BOLD)
            textLength = textLength - 1;
        return textLength;
    }
    private int userPrivilegeSelection(String role){
        int colorToParse = context.getResources().getColor(R.color.tkpd_dark_orange);
        switch (role){
            case "Penjual":
                colorToParse =  context.getResources().getColor(R.color.tkpd_status_red);
                break;
            case "Pembeli":
                colorToParse = context.getResources().getColor(R.color.label_buyer);
                break;
            case "Pengguna":
                colorToParse = context.getResources().getColor(R.color.label_user);
                break;
            case "Administrator":
                colorToParse = context.getResources().getColor(R.color.tkpd_dark_orange);
                break;
        }

        return colorToParse;
    }
}
