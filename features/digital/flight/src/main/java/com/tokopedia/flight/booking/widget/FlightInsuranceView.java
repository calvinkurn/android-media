package com.tokopedia.flight.booking.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.booking.view.adapter.FlightInsuranceBenefitAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceBenefitViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;

import java.util.List;

public class FlightInsuranceView extends LinearLayout {

    private AppCompatTextView tvName;
    private AppCompatTextView tvDescription;
    private AppCompatCheckBox cbInsurance;
    private AppCompatImageView ivHighlight;
    private AppCompatImageView ivProtectionImageView;
    private AppCompatTextView tvHighlight;
    private AppCompatTextView tvHighlightDetail;
    private AppCompatTextView tvHighlightTnc;
    private AppCompatTextView protectionLabelTextView;
    private RecyclerView benefitsRecyclerView;
    private LinearLayout highlightContainer;
    private LinearLayout otherProtection;
    private View dividerBenefit;

    private FlightInsuranceViewModel flightInsuranceViewModel;
    private ActionListener listener;

    public interface ActionListener {
        void onInsuranceChecked(FlightInsuranceViewModel insurance, boolean checked);

        void onMoreInfoClicked(String tncUrl, String title);

        void onBenefitExpanded();
    }

    public FlightInsuranceView(Context context) {
        super(context);
        init();
    }

    public FlightInsuranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlightInsuranceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlightInsuranceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), com.tokopedia.flight.R.layout.widget_flight_insurance_view, this);
        findView(view);
    }

    private void findView(View view) {
        tvName = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_name);
        tvDescription = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_description);
        cbInsurance = (AppCompatCheckBox) view.findViewById(com.tokopedia.flight.R.id.cb_insurance);
        ivHighlight = (AppCompatImageView) view.findViewById(com.tokopedia.flight.R.id.iv_highlight);
        ivProtectionImageView = (AppCompatImageView) view.findViewById(com.tokopedia.flight.R.id.iv_protection_expand);
        tvHighlight = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_highlight);
        tvHighlightDetail = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_highlight_detail);
        tvHighlightTnc = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_highlight_tnc);
        protectionLabelTextView = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_protection_label);
        benefitsRecyclerView = (RecyclerView) view.findViewById(com.tokopedia.flight.R.id.rv_benefits);
        highlightContainer = (LinearLayout) view.findViewById(com.tokopedia.flight.R.id.highlight_container);
        otherProtection = (LinearLayout) view.findViewById(com.tokopedia.flight.R.id.other_protection);
        dividerBenefit = (View) view.findViewById(com.tokopedia.flight.R.id.divider_benefit);
        protectionLabelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                benefitsRecyclerView.setVisibility(benefitsRecyclerView.getVisibility() == VISIBLE ? GONE : VISIBLE);
                if (benefitsRecyclerView.getVisibility() == VISIBLE) {
                    ivProtectionImageView.setRotation(180);
                } else {
                    ivProtectionImageView.setRotation(0);
                    if (listener != null) {
                        listener.onBenefitExpanded();
                    }
                }
            }
        });
        cbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (listener != null) {
                    listener.onInsuranceChecked(flightInsuranceViewModel, b);
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        renderData(this.flightInsuranceViewModel);
    }

    public void renderData(FlightInsuranceViewModel insuranceViewModel) {
        if (insuranceViewModel == null) return;
        this.flightInsuranceViewModel = insuranceViewModel;
        tvName.setText(insuranceViewModel.getName());
        tvDescription.setText(insuranceViewModel.getDescription());
        cbInsurance.setChecked(flightInsuranceViewModel.isDefaultChecked());
        if (insuranceViewModel.getBenefits() != null && insuranceViewModel.getBenefits().size() > 0) {
            renderHighlightBenefit(insuranceViewModel);
        } else {
            highlightContainer.setVisibility(GONE);
        }
        tvHighlightTnc.setText(buildTncText(
                insuranceViewModel.getTncAggreement(),
                insuranceViewModel.getTncUrl(),
                insuranceViewModel.getName())
        );
        tvHighlightTnc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void renderHighlightBenefit(FlightInsuranceViewModel insuranceViewModel) {
        highlightContainer.setVisibility(VISIBLE);
        FlightInsuranceBenefitViewModel highlightBenefit = insuranceViewModel.getBenefits().get(0);
        tvHighlight.setText(highlightBenefit.getTitle());
        tvHighlightDetail.setText(highlightBenefit.getDescription());
        ImageHandler.loadImageWithoutPlaceholder(ivHighlight, highlightBenefit.getIcon(),
                ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.flight_ic_airline_default)
        );

        if (insuranceViewModel.getBenefits().size() > 1) {
            renderMoreBenefit(insuranceViewModel.getBenefits().subList(1, insuranceViewModel.getBenefits().size()));
        } else {
            dividerBenefit.setVisibility(GONE);
            otherProtection.setVisibility(GONE);
        }
    }

    private void renderMoreBenefit(List<FlightInsuranceBenefitViewModel> benefits) {
        if (benefits.size() > 0) {
            otherProtection.setVisibility(VISIBLE);
            protectionLabelTextView.setText(String.format(getContext().getString(com.tokopedia.flight.R.string.flight_insurance_additional_benefits_prefix), benefits.size()));
            benefitsRecyclerView.setVisibility(GONE);
            FlightInsuranceBenefitAdapter benefitAdapter = new FlightInsuranceBenefitAdapter(benefits);
            benefitsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            benefitsRecyclerView.setAdapter(benefitAdapter);
            dividerBenefit.setVisibility(VISIBLE);
        } else {
            dividerBenefit.setVisibility(GONE);
            otherProtection.setVisibility(GONE);
        }
    }

    private SpannableStringBuilder buildTncText(String tncAggreement, String tncUrl, String title) {
        SpannableStringBuilder descriptionStr = setMoreInfoToBold(tncAggreement, tncUrl, title);
        setAsteriskToBold(tncAggreement, descriptionStr);
        return descriptionStr;
    }

    @NonNull
    private SpannableStringBuilder setMoreInfoToBold(String tncAggreement, String tncUrl, String title) {
        final int color = getResources().getColor(com.tokopedia.design.R.color.green_300);
        String fullText = String.format("%s. ", tncAggreement);
        if (tncUrl != null && tncUrl.length() > 0) {
            fullText += getContext().getString(com.tokopedia.flight.R.string.flight_insurance_learn_more_label);
        }
        int stopIndex = fullText.length();
        SpannableStringBuilder descriptionStr = new SpannableStringBuilder(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (listener != null && tncUrl != null && tncUrl.length() > 0) {
                    listener.onMoreInfoClicked(tncUrl, title);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            }
        };
        descriptionStr.setSpan(clickableSpan, tncAggreement.length() + 2, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return descriptionStr;
    }

    private void setAsteriskToBold(String tncAggreement, SpannableStringBuilder descriptionStr) {
        String asterisk = "*";
        int firstAsterisk = tncAggreement.indexOf(asterisk);
        int lastAsterisk = tncAggreement.lastIndexOf(asterisk);
        if (firstAsterisk != -1 && lastAsterisk != -1 && firstAsterisk != lastAsterisk) {
            CharacterStyle asteriskStyle = new CharacterStyle() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(com.tokopedia.design.R.color.font_black_secondary_54));
                    ds.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
                }
            };
            descriptionStr.delete(lastAsterisk, lastAsterisk + 1);
            descriptionStr.delete(firstAsterisk, firstAsterisk + 1);
            descriptionStr.setSpan(asteriskStyle, firstAsterisk, lastAsterisk, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
