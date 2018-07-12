package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.topads.sdk.R;


/**
 * Created by stevenfredian on 5/29/17.
 */

public class TopAdsInfoBottomSheet {

    public static String ADS_URL = "https://www.tokopedia.com/iklan?source=tooltip&medium=android";

    public static final String TAG = "TAIBS";
    private BottomSheetDialog dialog;
    private View closeButton;
    private View moreButton;

    public TopAdsInfoBottomSheet() {
    }

    public static TopAdsInfoBottomSheet newInstance(Context context) {
        TopAdsInfoBottomSheet frag = new TopAdsInfoBottomSheet();
        frag.dialog = new BottomSheetDialog(context);
        frag.dialog.setContentView(R.layout.promoted_info_dialog);
        frag.closeButton = frag.dialog.findViewById(R.id.close_but);
        frag.moreButton = frag.dialog.findViewById(R.id.more);
        frag.setView(context);
        return frag;
    }

    public void setView(final Context context) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ADS_URL));
                context.startActivity(intent);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
