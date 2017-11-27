package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksGroupsModel {
    /**
     * name : Top Picks
     * toppicks : [{"name":"Lebih Cepat Lebih Baik","image_url":"https://ecs7.tokopedia.net/img/cache/300-square/attachment/2017/9/27/7492183/7492183_194f02f2-a1c3-4aa6-8acd-7b56bee3d784.jpg","image_landscape_url":"https://ecs7.tokopedia.net/img/cache/315x165","url":"https://www.tokopedia.com/toppicks/lebih-cepat-lebih-baik/?device=ios","item":[{"id":0,"image_url":"https://ecs7.tokopedia.net/img/cache/300-square/attachment/2017/9/27/7492183/7492183_1792374e-84ce-4846-8853-b071a0b62f65.jpg","image_landscape_url":"https://ecs7.tokopedia.net/img/cache/315x165/attachment/2017/9/27/7492183/7492183_fff06894-abd8-4be8-880c-657d23f7404a.jpg","name":"Multitasking Tanpa Kendala","url":"https://www.tokopedia.com/catalog/53523/corsair-ddr4-vengeance-lpx-8-gb-2x4gb-cmk8gx4m2a2666c16?device=ios"},{"id":0,"image_url":"https://ecs7.tokopedia.net/img/cache/300-square/attachment/2017/9/27/7492183/7492183_863ef4e3-6968-4362-807d-ca8d5337a954.jpg","image_landscape_url":"https://ecs7.tokopedia.net/img/cache/315x165/attachment/2017/9/27/7492183/7492183_ffa83aa6-e72d-4da4-ab6d-461d550922b1.jpg","name":"Satukan Kesempurnaan","url":"https://www.tokopedia.com/catalog/51310/asus-maximus-viii-hero?device=ios"},{"id":0,"image_url":"https://ecs7.tokopedia.net/img/cache/300-square/attachment/2017/9/27/7492183/7492183_418a10c2-87ed-473a-8870-841eaf78aef8.jpg","image_landscape_url":"https://ecs7.tokopedia.net/img/cache/315x165/attachment/2017/9/27/7492183/7492183_4177d89e-88c6-44fd-9360-fc6bde168df8.jpg","name":"Performa Lebih Maksimal","url":"https://www.tokopedia.com/catalog/55020/intel-core-i9-7900x?device=ios"}]}]
     */

    @SerializedName("name")
    private String name;
    @SerializedName("toppicks")
    private List<TopPicksModel> toppicks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TopPicksModel> getToppicks() {
        return toppicks;
    }

    public void setToppicks(List<TopPicksModel> toppicks) {
        this.toppicks = toppicks;
    }

}
