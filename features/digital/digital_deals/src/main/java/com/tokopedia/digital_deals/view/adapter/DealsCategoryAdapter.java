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
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;

import java.util.List;

public class DealsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private boolean isFooterAdded = false;



    public DealsCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.deals_item_card, parent, false);
                holder = new ItemViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((ItemViewHolder) holder).bindData(categoryItems.get(position), position);
                ((ItemViewHolder) holder).setIndex(position);
                break;
            case FOOTER:
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new CategoryItemsViewModel());
        }
    }

    public void add(CategoryItemsViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<CategoryItemsViewModel> items) {
        for (CategoryItemsViewModel item : items) {
            add(item);
        }
    }


    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            CategoryItemsViewModel item = categoryItems.get(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        private int mPosition;

        public ItemViewHolder(View itemView) {
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

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel, int position) {
//            discount.setText(categoryItemsViewModel.getd);
            mPosition=position;
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

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout=itemView.findViewById(R.id.loading_fl);
        }
    }

}
