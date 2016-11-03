package com.tokopedia.tkpd.talk.inboxtalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.talk.inboxtalk.fragment.InboxTalkFragment;

import java.util.Objects;

/**
 * Created by stevenfredian on 5/18/16.
 */
public class InboxTalkFilterDialog {
    private final Spinner filterRead;
    private final Button submit;
    private final Activity activity;
    private final InboxTalkFragment fragment;
    private final BottomSheetDialog dialog;

    private ArrayAdapter<CharSequence> adapterRead;

    private enum filterString{
        STRING_ALL("all"), STRING_UNREAD("unread");

        private final String text;
        filterString(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    public InboxTalkFilterDialog(Activity activity, InboxTalkFragment inboxTalkFragment) {
        this.activity = activity;
        this.fragment = inboxTalkFragment;
        this.dialog = new BottomSheetDialog(activity);
        this.dialog.setContentView(R.layout.inbox_talk_filter_dialog);
        filterRead = (Spinner) dialog.findViewById(R.id.filter_status_read);
        submit = (Button) dialog.findViewById(R.id.submit);
        initAdapter();
        setAdapter();
    }

    public static InboxTalkFilterDialog Builder(Activity activity, InboxTalkFragment inboxTalkFragment) {
        return new InboxTalkFilterDialog(activity,inboxTalkFragment);
    }

    public void initAdapter() {
        adapterRead = ArrayAdapter.createFromResource(activity, R.array.talk_read, R.layout.dialog_item);
        adapterRead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setAdapter() {
        filterRead.setAdapter(adapterRead);
    }

    public void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                fragment.filter(
                        filterString.values()[filterRead.getSelectedItemPosition()].toString());
            }
        });
    }

    public void setView() {
        filterRead.setPrompt("Status Terbaca");
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout)
                        dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
    }

    public void setSelection(String string) {
        int item=0;
        for(filterString filter : filterString.values()){
            if(string.equals(filter.toString())){
                item = filter.ordinal();
            }
        }
        filterRead.setSelection(item);
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
