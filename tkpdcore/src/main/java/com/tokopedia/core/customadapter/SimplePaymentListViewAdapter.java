package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

import java.util.ArrayList;

public class SimplePaymentListViewAdapter extends BaseAdapter {
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<Integer> Res = new ArrayList<Integer>();
    private ArrayList<Bitmap> Image = new ArrayList<Bitmap>();
    private ArrayList<String> feeList = new ArrayList<>();
    private ArrayList<String> imageURL = new ArrayList<>();
    public Boolean isMain = false;
    public Boolean AllowSaldo;
    public ArrayList<ImageView> listImageView = new ArrayList<>();

    public Activity context;
    public LayoutInflater inflater;

    public SimplePaymentListViewAdapter(Activity context, ArrayList<String> name, ArrayList<Integer> Res, ArrayList<String> fee, Boolean AllowSaldo) {
        super();

        this.context = context;
        this.name = name;
        this.Res = Res;
        this.AllowSaldo = AllowSaldo;
        this.feeList = fee;
        //this.Image = pImage;


        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SimplePaymentListViewAdapter(Activity context, ArrayList<String> name, ArrayList<Integer> Res, Boolean AllowSaldo) {
        super();

        this.context = context;
        this.name = name;
        this.Res = Res;
        this.AllowSaldo = AllowSaldo;
        //this.Image = pImage;


        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SimplePaymentListViewAdapter(Activity context, ArrayList<String> name, ArrayList<Integer> Res, ArrayList<String> fee, ArrayList<String> imageURL, Boolean AllowSaldo) {
        super();

        this.context = context;
        this.name = name;
        this.Res = Res;
        this.AllowSaldo = AllowSaldo;
        this.feeList = fee;
        this.imageURL = imageURL;
        //this.Image = pImage;


        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public void AddBitmap(Bitmap pImage) {
        Image.add(pImage);
    }

    public static class ViewHolder {
        ImageView pImageView;
        TextView pNameView;
        View Main;
        TextView paymentFee;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null || !isMain) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_payment_listview, null);

            holder.pImageView = (ImageView) convertView.findViewById(R.id.img);
            holder.pNameView = (TextView) convertView.findViewById(R.id.name);
            holder.Main = convertView.findViewById(R.id.main);
            holder.paymentFee = (TextView) convertView.findViewById(R.id.payment_fee);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        try {
            if (imageURL.isEmpty()) {
                if (Image.size() != 0) {
                    holder.pImageView.setImageBitmap(Image.get(position));
                } else if (Res.size() != 0) {
                    holder.pImageView.setImageResource(Res.get(position));
                }
            } else {
                ImageHandler.LoadImage(holder.pImageView, imageURL.get(position));
                listImageView.add(holder.pImageView);
            }

        } catch (Exception e) {

        }
        holder.pNameView.setText(name.get(position));
        if (feeList.size() != 0)
            holder.paymentFee.setText(feeList.get(position));
        return convertView;
    }

    public ImageView getPaymentImage(int position) {
        return listImageView.get(position);
    }
}