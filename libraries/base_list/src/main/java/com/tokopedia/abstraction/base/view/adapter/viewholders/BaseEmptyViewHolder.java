package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.baselist.R;


/**
 * @author kulomady on 1/24/17.
 */

public class BaseEmptyViewHolder<T extends EmptyModel> extends AbstractViewHolder<T> {

    protected ImageView emptyIconImageView;
    protected TextView emptyTitleTextView;
    protected TextView emptyContentTextView;
    protected TextView emptyContentItemTextView;
    protected Button emptyButtonItemButton;
    private Callback callback;
    private Context context;

    public BaseEmptyViewHolder(View itemView) {
        super(itemView);
        findView(itemView);
    }

    public BaseEmptyViewHolder(View itemView, Callback callback) {
        super(itemView);
        findView(itemView);
        this.callback = callback;
    }

    protected void findView(View itemView) {
        context = itemView.getContext();
        emptyTitleTextView = (TextView) itemView.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = (TextView) itemView.findViewById(R.id.text_view_empty_content_text);
        emptyContentItemTextView = (TextView) itemView.findViewById(R.id.text_view_empty_content_item_text);
        emptyButtonItemButton = (Button) itemView.findViewById(R.id.button_add_promo);
        emptyIconImageView = (ImageView) itemView.findViewById(R.id.no_result_image);
    }

    @Override
    public void bind(T element) {
        if (element.getIconRes() != 0) {
            emptyIconImageView.setImageDrawable(MethodChecker.getDrawable(context, element.getIconRes()));
        }
        if (!TextUtils.isEmpty(element.getUrlRes())) {
            if (context != null) {
                Glide.with(context).load(element.getUrlRes()).into(emptyIconImageView);
            }
        }
        if (element.getCallback() != null) {
            callback = element.getCallback();
        }
        if (TextUtils.isEmpty(element.getTitle())) {
            emptyTitleTextView.setVisibility(View.GONE);
        } else {
            emptyTitleTextView.setText(element.getTitle());
            emptyTitleTextView.setVisibility(View.VISIBLE);
        }
        if (element.getContentRes() != 0) {
            emptyContentTextView.setText(element.getContentRes());
            emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(element.getContent())) {
                emptyContentTextView.setText(element.getContent());
                emptyContentTextView.setVisibility(View.VISIBLE);
            } else {
                emptyContentTextView.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(element.getDescription())) {
            emptyContentItemTextView.setVisibility(View.VISIBLE);
            emptyContentItemTextView.setText(element.getDescription());
            emptyContentItemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onEmptyContentItemTextClicked();
                    }
                }
            });
        } else {
            emptyContentItemTextView.setVisibility(View.GONE);
        }
        if (element.getButtonTitleRes() != 0) {
            emptyButtonItemButton.setText(element.getButtonTitleRes());
            emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onEmptyButtonClicked();
                    }
                }
            });
            emptyButtonItemButton.setVisibility(View.VISIBLE);
        } else {
            if (TextUtils.isEmpty(element.getButtonTitle())) {
                emptyButtonItemButton.setVisibility(View.GONE);
            } else {
                emptyButtonItemButton.setText(element.getButtonTitle());
                emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback != null) {
                            callback.onEmptyButtonClicked();
                        }
                    }
                });
                emptyButtonItemButton.setVisibility(View.VISIBLE);
            }
        }
    }


    public interface Callback {

        void onEmptyContentItemTextClicked();

        void onEmptyButtonClicked();
    }

}
