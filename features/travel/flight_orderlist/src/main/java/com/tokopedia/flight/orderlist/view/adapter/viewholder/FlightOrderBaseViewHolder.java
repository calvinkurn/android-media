package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderSimpleAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.OrderSimpleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 12/13/17.
 */

public abstract class FlightOrderBaseViewHolder<T extends Visitable> extends AbstractViewHolder<T> {
    private AppCompatImageView ivOverflow;
    private AppCompatImageView ivJourneyArrow;
    private RecyclerView rvDepartureSchedule;


    public FlightOrderBaseViewHolder(View itemView) {
        super(itemView);
        initViewListener(itemView);
    }

    private void initViewListener(View itemView) {
        ivOverflow = (AppCompatImageView) itemView.findViewById(R.id.iv_overflow);
        ivJourneyArrow = (AppCompatImageView) itemView.findViewById(R.id.iv_arrow);
        rvDepartureSchedule = (RecyclerView) itemView.findViewById(R.id.rv_departure_schedule);
        ivOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailOptionClicked();
            }
        });
    }

    protected void renderArrow(List<FlightOrderJourney> orderJourneys) {
        if (orderJourneys.size() > 1) {
            setDoubleArrow();
        } else {
            setSingleArrow();
        }
    }

    protected void setDoubleArrow() {
        ivJourneyArrow.setImageResource(R.drawable.icon_arrow_double_edge);
    }

    protected void setSingleArrow() {
        ivJourneyArrow.setImageResource(R.drawable.ic_arrow_right_flight);
    }

    protected CharSequence getAirportTextForView(String cityName) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(cityName)) {
            text.append(cityName);
            makeBold(itemView.getContext(), text);
        }
        return text;
    }

    private SpannableStringBuilder makeSmall(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new RelativeSizeSpan(0.6f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private SpannableStringBuilder makeBold(Context context, SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.0f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)),
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    protected void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_flight_order, popup.getMenu());
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked());
        popup.show();
    }

    protected abstract void onHelpOptionClicked();

    protected abstract void onDetailOptionClicked();

    protected void renderDepartureSchedule(FlightOrderJourney orderJourney) {
        List<FlightOrderJourney> journeys = new ArrayList<>();
        journeys.add(orderJourney);
        renderDepartureSchedule(journeys);
    }

    protected void renderDepartureSchedule(List<FlightOrderJourney> orderJourney) {
        FlightOrderSimpleAdapter departureSchedules = new FlightOrderSimpleAdapter();
        departureSchedules.setDescriptionTextColor(itemView.getContext().getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
        departureSchedules.setTitleHalfView(false);
        departureSchedules.setContentAllignmentLeft(true);
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        rvDepartureSchedule.setLayoutManager(flightSimpleAdapterLayoutManager);
        rvDepartureSchedule.setHasFixedSize(true);
        rvDepartureSchedule.setNestedScrollingEnabled(false);
        rvDepartureSchedule.setAdapter(departureSchedules);
        if (orderJourney.size() == 1) {
            OrderSimpleViewModel simpleViewModel = new OrderSimpleViewModel();
            simpleViewModel.setLabel(TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.FORMAT_DATE, orderJourney.get(0).getDepartureTime()));
            simpleViewModel.setDescription(null);
            departureSchedules.setTitleBold(false);
            departureSchedules.setArrowVisible(false);
            departureSchedules.setTitleOnly(true);
            departureSchedules.setViewModel(simpleViewModel);
            departureSchedules.notifyDataSetChanged();
        } else {
            List<OrderSimpleViewModel> simpleViewModels = new ArrayList<>();
            int index = 0;
            for (FlightOrderJourney journey : orderJourney) {
                simpleViewModels.add(new OrderSimpleViewModel(journey.getDepartureAiportId() + "-" + journey.getArrivalAirportId(),
                        TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.FORMAT_DATE, orderJourney.get(index).getDepartureTime())));
                index++;
            }
            departureSchedules.setTitleBold(true);
            departureSchedules.setArrowVisible(false);
            departureSchedules.setViewModels(simpleViewModels);
            departureSchedules.notifyDataSetChanged();
        }
    }

    protected class OnMenuPopupClicked implements PopupMenu.OnMenuItemClickListener {

        OnMenuPopupClicked() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_detail) {
                onDetailOptionClicked();
                return true;
            } else if (item.getItemId() == R.id.action_help) {
                onHelpOptionClicked();
                return true;
            } else {
                return false;
            }
        }
    }
}
