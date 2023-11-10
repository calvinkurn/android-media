---
title: "Category Nav Bottomsheet"
---






| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Project Lead** | [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence)  |
| Team | [Darth](https://tokopedia.atlassian.net/people/team/8c90de56-d4f1-45a7-9021-bd87c4ea9ce2) ([Mayank Gera](https://tokopedia.atlassian.net/wiki/people/5f9281a8f162650070dacffd?ref=confluence) ) |
| Release date |  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | [PRD - Category Navigation Bottom Sheet Global Component](/wiki/spaces/CT/pages/989139013/PRD+-+Category+Navigation+Bottom+Sheet+Global+Component)  |
| Module Location |  `features/category/category_navbottomsheet` |

## Table of Contents

<!--toc-->

## Overview

### Background

As a user, when I am on page where we need category navigation, I should see **Category Navigation BottomSheet** with the ability to anchor to the preselected L2/L3.

### Project Description

1. Category navigation bottom sheet consists of L1, L2, and L3 categories

. We get these categories from GQL `categoryAllList`.
2. On the category navigation bottom sheet, if categoryID of an L2/ L3 is passed , then the L1 category of the preselected L2/L3 category should be highlighted (as an example in screenshots, in Discovery Page

, category of Buku Agama Islam(L3) which is passed to bottomsheet is selected, its L2 - Religi & Spiritual is shown as expanded and on the category navigation, the L1 of Religi & Spiritual category, which is Buku should be selected). Highlighted L1 category should be placed at the top of the L1 list.
3. When user clicks on other L1 category that is not initially selected, the page should show all L2 categories without expanding any L3 categories under those L2 categories. More functionalities can be found in PRD attached above.

## How-to

Add the dependency for adding Category Navigation BottomSheet in your module



```
implementation projectOrAar(rootProject.ext.features.categoryNavBottomSheet)
```


To show the bottom sheet , you need to get an instance

for the sheet.



```
CategoryNavBottomSheet.getInstance(
  selectedCatId: String, // Category id that needs to be shown selected.
  categoryListener: CategorySelected? = null, 
  gtmProviderListener: GtmProviderListener? = null, //optional
  shouldHideL1View: Boolean = false, // boolean to choose if you want to show L1 category views as well
  source : String = DEFAULT_SOURCE // source to be sent to GQL. DEFAULT_SORUCE = not-best-seller
 )
```


Listeners to be passed and used :



```
interface CategorySelected {
  fun onCategorySelected(
    catId: String, //CategoryID of L2/L3 selected
    appLink: String?, // Applink for the category 
    depth: Int, // 2 - for L2, 3 - for L3
    catName : String // Name of the category.
  )
}
```



```
interface GtmProviderListener {
  fun onBottomSheetOpen() {}
  fun onBottomSheetClosed() {}
  fun onL1Clicked(id: String?, name: String?) {}
  fun onL2Clicked(id: String?, name: String?) {}
  fun onL3Clicked(id: String?, name: String?) {}
  fun onL2Expanded(id: String?, name: String?) {}
  fun onL2Collapsed(id: String?, name: String?) {}
}
```

## Navigation :







![](res/Best%20Seller%20Entry.gif)





![](res/CLP%20Entry.gif)







## Screenshots:







![](res/Screenshot%202023-01-02%20at%201.58.56%20PM.png)

With L1 category showing





![](res/Screenshot%202023-01-02%20at%201.59.58%20PM.png)

With L1 category Hidden









---




