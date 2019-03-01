package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.data.model.BadCsatReasonListItem;
import com.tokopedia.contactus.inboxticket2.di.DaggerInboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxComponent;
import com.tokopedia.contactus.inboxticket2.di.InboxModule;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomQuickOptionView;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FragmentProvideRating extends BaseDaggerFragment implements ProvideRatingContract.ProvideRatingView {

    @Inject
    ProvideRatingContract.ProvideRatingPresenter presenter;

    InboxComponent component;
    private TextView mTxtHelpTitle;
    private LinearLayout mSmileLayout;
    private TextView mTxtSmileSelected;
    private TextView mTxtFeedbackQuestion;
    private TextView mTxtFinished;
    public static final String CLICKED_EMOJI = "clicked_emoji";
    public static final String PARAM_TICKET_ID = "ticket_id";
    public static final String PARAM_COMMENT_ID = "comment_id";
    private CustomQuickOptionView mFilterReview;

    public static FragmentProvideRating newInstance(Bundle bundle) {
        FragmentProvideRating fragment = new FragmentProvideRating();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        component = DaggerInboxComponent.builder()
                .inboxModule(new InboxModule(getContext()))
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_provide, container, false);
        initView(view);
        presenter = component.getProvideRatingPresenter();
        presenter.attachView(this);
        disableSubmitButton();
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void setFirstEmoji(int drawable) {
        addImageView(drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFirstEmojiClick();
            }
        });
    }

    @Override
    public void setSecondEmoji(int drawable) {
        addImageView(drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSecondEmojiClick();
            }
        });
    }

    @Override
    public void setThirdEmoji(int drawable) {
        addImageView(drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onThirdEmojiClick();
            }
        });
    }

    @Override
    public void setFourthEmoji(int drawable) {
        addImageView(drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFourthEmojiClick();
            }
        });
    }

    @Override
    public void setFifthEmoji(int drawable) {
        addImageView(drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFifthEmojiClick();
            }
        });
    }

    @Override
    public void setMessage(String message) {
        mTxtSmileSelected.setText(message);
    }

    @Override
    public void setMessageColor(String color) {
        mTxtSmileSelected.setTextColor(Color.parseColor(color));
    }

    @Override
    public void setQuestion(String question) {
        mTxtFeedbackQuestion.setText(question);
    }

    private ImageView addImageView(int drawable) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1.0f;
        imageView.setImageResource(drawable);
        imageView.setLayoutParams(layoutParams);
        mSmileLayout.addView(imageView);
        return imageView;
    }




    @Override
    public int getSelectedEmoji() {
        return getArguments().getInt(CLICKED_EMOJI);
    }

    @Override
    public void clearEmoji() {
        mSmileLayout.removeAllViews();
    }

    @Override
    public void showErrorMessage(String o) {
        ToasterError.make(getView(), o).show();
    }

    List<String> selectedOption = new ArrayList<>();
    @Override
    public void setFilterList(List<BadCsatReasonListItem> filterList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mFilterReview.updateLayoutManager(gridLayoutManager);
        List<QuickFilterItem> filterItems = new ArrayList<>();
        for(BadCsatReasonListItem filter:filterList) {
            CustomViewQuickFilterItem finishFilter = new CustomViewQuickFilterItem();
            finishFilter.setName(filter.getMessage());
            finishFilter.setType(filter.getId()+"");
            finishFilter.setColorBorder(R.color.tkpd_main_green);
            filterItems.add(finishFilter);
        }
        mFilterReview.renderFilter(filterItems);
        mFilterReview.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                if(selectedOption.contains(typeFilter)) {
                    selectedOption.remove(typeFilter);
                }else {
                    selectedOption.add(typeFilter);
                }

                if( mFilterReview.isAnyItemSelected()) {
                    enableSubmitButton();
                }else {
                    disableSubmitButton();
                }
            }
        });
    }


    @Override
    public String getTicketId() {
        return getArguments().getString(PARAM_TICKET_ID);
    }

    @Override
    public String getSelectedItem() {
        String filters = "";
        for (String filter:selectedOption) {
            filters +=filter+";";
        }
        filters = filters.substring(0,filters.length()-1);
        return filters;
    }

    @Override
    public String getCommentId() {
        return getArguments().getString(PARAM_COMMENT_ID);
    }

    @Override
    public void onSuccessSubmit() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void initView(View view) {
        mTxtHelpTitle = view.findViewById(R.id.txt_help_title);
        mSmileLayout = view.findViewById(R.id.smile_layout);
        mTxtSmileSelected = view.findViewById(R.id.txt_smile_selected);
        mTxtFeedbackQuestion = view.findViewById(R.id.txt_feedback_question);
        mTxtFinished = view.findViewById(R.id.txt_finished);
        mFilterReview = view.findViewById(R.id.filter_review);
        mTxtFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSubmitClick();
            }
        });
    }

    public void disableSubmitButton() {
        MethodChecker.setBackground(mTxtFinished, MethodChecker.getDrawable(getContext(), R.drawable
                .bg_button_disabled));
        mTxtFinished.setTextColor(MethodChecker.getColor(getContext(), R.color.grey_500));
        mTxtFinished.setEnabled(false);
    }

    public void enableSubmitButton() {
        MethodChecker.setBackground(mTxtFinished, MethodChecker.getDrawable(getContext(), R.drawable
                .button_curvy_green));
        mTxtFinished.setTextColor(MethodChecker.getColor(getContext(), R.color.white));
        mTxtFinished.setEnabled(true);
    }
}
