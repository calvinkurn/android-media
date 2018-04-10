package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

public class DealsCategoryAdapter extends RecyclerView.Adapter<DealsCategoryAdapter.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;

    public DealsCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView discount;
        private TextView textView1;
        private TextView dealsDetails;
        private TextView dealOfferer;
        private TextView dealavailableLocations;
        private TextView dealSoldNumberTimes;
        private TextView dealListPrice;
        private TextView dealSellingPrice;
        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            discount = itemView.findViewById(R.id.textView2);
            dealsDetails = itemView.findViewById(R.id.textView3);
            dealOfferer = itemView.findViewById(R.id.textView4);
            dealavailableLocations = itemView.findViewById(R.id.textView5);
            dealListPrice = itemView.findViewById(R.id.textView6);
            dealSoldNumberTimes = itemView.findViewById(R.id.textView7);
            dealSellingPrice = itemView.findViewById(R.id.textView8);
        }

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel) {
//            discount.setText(categoryItemsViewModel.getd);
            dealsDetails.setText(categoryItemsViewModel.getDisplayName());
            dealSoldNumberTimes.setText("Terjual " + categoryItemsViewModel.getSoldQuantity() + " kali");
            ImageHandler.loadImageCover2(dealImage, categoryItemsViewModel.getImageWeb());

            dealListPrice.setText("Rp " + categoryItemsViewModel.getMrp());
            dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            dealSellingPrice.setText("Rp " + categoryItemsViewModel.getSalesPrice());
            itemView.setOnClickListener(this);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
//            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
//            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
//            detailsIntent.putExtra("homedata", categoryItems.get(mViewHolder.getIndex()));
//            context.startActivity(detailsIntent);
        }
    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deals_item_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(categoryItems.get(position));
        holder.setIndex(position);
    }

}
