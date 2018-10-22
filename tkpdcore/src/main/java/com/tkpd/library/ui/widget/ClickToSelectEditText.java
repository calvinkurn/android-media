package com.tkpd.library.ui.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.core2.R;

import java.util.List;

/**
 * Created by noiz354 on 4/18/16.
 * Modify by Sebast on 7/27/16 adding dialog fragment for dialog custom view
 */
public class ClickToSelectEditText <T extends Listable> extends EditText {
    List<T> mItems;
    String[] mListableItems;
    CharSequence mHint;
    AlertDialog alertDialog;

    OnItemSelectedListener<T> onItemSelectedListener;

    public ClickToSelectEditText(Context context) {
        super(context);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHint = getHint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mHint = getHint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public List<T> getmItems() {
        return mItems;
    }

    public void setmItems(List<T> mItems) {
        this.mItems = mItems;
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        this.mListableItems = new String[items.size()];

        int i = 0;

        for (T item : mItems) {
            mListableItems[i++] = item.getLabel();
        }

        configureOnClickListener();
    }

    private void configureOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mHint);


                /**
                 * new one
                 */
                CutsomArrayAdapter cutsomArrayAdapter = new CutsomArrayAdapter(getContext(),R.layout.dialog_chooser,mListableItems,getText().toString());
                builder.setAdapter(cutsomArrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {

                            }
                        });

                /**
                 * Old one
                 */
//                builder.setItems(mListableItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
//                        setText(mListableItems[selectedIndex]);
//
//                        if (onItemSelectedListener != null) {
//                            onItemSelectedListener.onItemSelectedListener(mItems.get(selectedIndex), selectedIndex);
//                        }
//                    }
//                });

                builder.setPositiveButton(getResources().getString(R.string.title_close), null);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }

    public class CutsomArrayAdapter extends ArrayAdapter{

        private LayoutInflater mInflater;

        private String[] mStrings;

        private int mViewResourceId;

        private String selected;

        public CutsomArrayAdapter(Context context, int viewResourceId, String[] strings, String selected) {
            super(context, viewResourceId, strings);
            mInflater = ((AppCompatActivity)context).getLayoutInflater();
            mViewResourceId = viewResourceId;
            mStrings = strings;
            this.selected = selected;
        }
        @Override
        public int getCount() {
            return mStrings.length;
        }

        @Override
        public String getItem(int position) {
            return mStrings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(mViewResourceId, parent, false);

            convertView.setMinimumHeight(132);
            TextView tv = (TextView)convertView.findViewById(R.id.simple_text_view); //Give Id to your textview
            tv.setText(mStrings[position]);
            if(mStrings[position].equals(selected)) {
                RadioButton r = (RadioButton) convertView.findViewById(R.id.simple_radio_button);
                r.setChecked(true);
            }

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    setText(mListableItems[position]);

                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onItemSelectedListener(mItems.get(position), position);
                    }

                    alertDialog.dismiss();
                }
            });

            return convertView;
        }

    }
}
