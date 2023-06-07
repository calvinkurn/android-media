---
title: "Discovery Pages"
labels:
- android
- discovery
- widget-architecture
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Project Lead** | [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence)  |
| Product Manager | [Nadya Putri Novanti](https://tokopedia.atlassian.net/wiki/people/5d117997c010590ca394ef49?ref=confluence) [Jessica Lesmana](https://tokopedia.atlassian.net/wiki/people/604ef14760d39e006f46adeb?ref=confluence) [Arvin Wahyu Septiandie](https://tokopedia.atlassian.net/wiki/people/62535dd5a8727100691f9f92?ref=confluence) [Kinanti Pratami](https://tokopedia.atlassian.net/wiki/people/6062b7c8ea084b0069f9c8b9?ref=confluence)  |
| Team | [Darth](https://tokopedia.atlassian.net/people/team/8c90de56-d4f1-45a7-9021-bd87c4ea9ce2) |
| Release date |  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | <https://docs.google.com/presentation/d/1BYx5ynE8FFwekdrXche2VZLzKJ84bJRyDc9S5N8sAbg/edit#slide=id.g7fb4aa40f9_0_2296>  |
| Module Location | `features/category/discovery2/src/main/java/com/tokopedia/discovery2` |

## Table of Contents

<!--toc-->

## Overview

The Discovery page is a dynamic page that offers numerous possibilities for component combinations. This is because the Discovery page is used by various business teams as a landing page for their marketing campaigns.

As a landing page, the Discovery page serves as a gateway for users to explore the various products and services offered on the Tokopedia platform. To enhance the user experience, the page is designed to be highly customizable so that each campaign can have a unique look and feel that reflects the brand and message of the campaign.

![](res/Screenshot%202023-02-06%20at%202.37.00%20PM.png)

### Background

The Android widget architecture is designed to be highly flexible and extensible, allowing developers/product team to create widgets that can be customized in a wide variety of ways.

By using dynamic pages in their widgets, product team can create highly interactive and engaging user experiences, without having to build a separate app for each piece of functionality. This can help to streamline the development process and make it easier for product teams to manage and update the app over time.

Overall, the Android widget architecture is a powerful and flexible platform for building highly customized and engaging user experiences, with dynamic pages being a key feature that allows developers to create widgets that are both functional and easy to use.

### Project Description

In Android, Discovery pages are using similar concept like multi-viewtypes in a recylerview but here we have different viewholders which are parsed based on their `component_name` from Backend.   
In a RecyclerView with multiple view types, the `DiscoveryHomeFactory` defines a set of view holders, each corresponding to a different type of content. When the RecyclerView displays a new item, the adapter determines which view holder to use based on the item's `component_name` and inflates the appropriate view holder.  
One advantage of this approach is that it allows for greater flexibility in how the data is displayed. Rather than being limited to a fixed set of view holders, developers can create dynamic layouts that can adapt to different types of data. This can be especially useful in cases where the data is highly variable.

### Tech Stack

- Lottie
- Widget Architecture

### Project Architecture/Flow Diagram (Widget Architecture)

![](res/Untitled%20Diagram.drawio.png)

## **HTTP Request (GQL)**

## *Main Queries for the whole Discovery page*

![](res/DiscoveryPage%20network%20flow.drawio.png)



| **GQL Query** | **Usage** |
| --- | --- |
| DiscoPageLayoutQuery<br/> | To obtain the whole layout of the Discovery page along with the data, query require an `identifier` parameter that is obtained from the last part of the applink.For example, if the applink is `tokopedia://discovery/kejar-diskon`, the `identifier` will be `kejar-diskon`The result of this query is mapped based on the model in **DiscoveryResponse.kt** |
| ComponentInfo | To obtain the data of components that are dynamically loaded, which are any `product card` component (either the carousel or the infinite scrolling components), `claim_coupon`, `cpm_topads`, and`tokopoints` etc.The result of this query is mapped based on the model **ComponentsItem.kt** |
| discoverySectionInfo | To obtain the whole layout of the section along with the data of some of the components that are anchor components.  |

### Example on how to use the `gql.tokopedia.com/ui`

- Page Layout Query



```
query DiscoPageLayoutQuery($identifier: String!, $version: String!, $device: String!, $filters:String) {
    discoveryPageInfo(identifier: $identifier, version: $version, device: $device,filters: $filters) {
    data {
      title
      components
     page_info {
            Id
            Identifier
            Name
            Path
            Type
            share_config
            campaign_code
            search_applink
            search_title
            show_choose_address
            tokonow_has_mini_cart_active
          }
      additional_info
    }
  }
}

----------------------------------------------------------------------

Query variables :

{
  "identifier": "kejar-diskon",
  "version": "3.196",
"device": "Android",
"filters": "rpc_UserCityId=176&rpc_UserDistrictId=1859&accept_section=true"
}
```

- Component Info Query



```
query ComponentInfo($device: String!, $component_id: String!, $identifier: String!, $filters: String) {
  componentInfo(device: $device, component_id: $component_id, identifier: $identifier, filters: $filters) {
  data
  }
}

----------------------------------------------------------------------

Query variables:
{
  "identifier": "kejar-diskon",
  "component_id": "9",
"device": "Android",
"filters": "rpc_page_size=10&rpc_page_number=1&rpc_next_page=&rpc_UserCityId=176&rpc_UserDistrictId=2274&rpc_UserWarehouseId=12210375"
}
```

- Discovery Section Info Query



```
query discoverySectionInfo($device: String!, $identifier: String!, $section_id: String!, $filters: String!) {
  discoverySectionInfo(device: $device, identifier: $identifier, section_id: $section_id, filters: $filters) {
    data
  }
}


----------------------------------------------------------------------

Query variables:
{
  "device": "Android",
  "identifier": "beli-lagi-landing-page-test-2",
  "section_id": "2003",
"filters": "rpc_UserAddressId=0&rpc_UserCityId=146&rpc_UserDistrictId=1859&rpc_UserWarehouseId=0"
}
```



---

## **Entry Points / Navigation**

Applink 👇🏻- tokopedia://discovery/kejar-diskon



| **S.No** | **How to Access** | **Video** |
| --- | --- | --- |
| 1. | You can access the Discovery page from Home, then click “promo Hari Ini” menu or you can scroll down to products / campaigns and click “lihat semua”  | [Screen Recording 2023-02-06 at 2.34.00 PM.mov](/wiki/download/attachments/2106098856/Screen%20Recording%202023-02-06%20at%202.34.00%20PM.mov?version=1&modificationDate=1675674371525&cacheVersion=1&api=v2&width=340)<br/> |
| 2. | You can also access the page directly using developer tools or test app page by inserting the discovery page applink i.e - tokopedia://discovery/{page name}(page shown in picture is - kejar diskon page)(applink of this page - tokopedia://discovery/kejar-diskon) | ![](res/Screenshot%202023-02-06%20at%202.37.00%20PM.png)<br/> |
| 3. | (page shown in picture is - toserba page)(applink of this page - tokopedia://discovery/toserba) | ![](res/Screenshot%202023-02-06%20at%202.37.44%20PM.png)<br/> |

## Useful Links

- <https://www.figma.com/file/W8PkEMVS3OlzruR1A4tPmA/Component-Automation---Discovery?node-id=338%3A52188>
- [Thanos](https://mynkama.tokopedia.com/)
- [HPD/DP - [API:GQL] GraphQL discoveryPageInfo](/wiki/spaces/HP/pages/2035779464)

