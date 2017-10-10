import { PosCacheModule } from 'NativeModules'


// ===================== Product List ======================= //
export const FETCH_PRODUCTS = 'FETCH_PRODUCTS'
export const fetchProducts = (shopId, start, rows, etalaseId, productId, queryText) => {
  return {
    type: FETCH_PRODUCTS,
    payload: PosCacheModule.getDataAll("PRODUCT")
      .then(response => {
        const jsonResponse = JSON.parse(response)
        return jsonResponse;
      })
      .catch(error => {})
  }
}

// ==================== Search Product ==================== //
export const SEARCH_PRODUCT = 'SEARCH_PRODUCT'
export const searchProduct = (product, etalaseId) => {
  return {
    type: SEARCH_PRODUCT,
    payload: ProductDiscoveryModule.search(`{ product: ${product}, etalase_id: ${etalaseId} }`)
                .then(res => {
                  const jsonResponse = JSON.parse(res)
                  console.log(jsonResponse)
                  return jsonResponse
                })
                .catch(err => {})
  }
}

export const FETCH_ETALASE = 'FETCH_ETALASE'
export const fetchEtalase = (shopId) => ({
  type: FETCH_ETALASE,
  payload: PosCacheModule.getDataAll("ETALASE")
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
    })
    .catch(err => console.log(err))
})


export const PULL_TO_REFRESH = 'PULL_TO_REFRESH'
export const pullToRefresh = () => {
  return {
    type: PULL_TO_REFRESH
  }
}


export const ON_ETALASE_CHANGE = 'ON_ETALASE_CHANGE'
export const onEtalaseChange = (id) => {
  return {
    type: ON_ETALASE_CHANGE,
    payload: id
  }
}


export const RESET_PRODUCT_LIST = 'RESET_PRODUCT_LIST'
export const resetProductList = () => {
  return {
    type: RESET_PRODUCT_LIST,
  }
}


// Search actions
export const ON_SEARCH_QUERY_TYPE = 'ON_SEARCH_QUERY_TYPE'
export const onSearchQueryType = (queryText) => {
  return {
    type: ON_SEARCH_QUERY_TYPE,
    payload: queryText
  }
}

export const FETCH_SEARCH_PRODUCT = 'FETCH_SEARCH_PRODUCT'
export const fetchSearchProduct = (eId, queryText, shopId) => {
  const text = queryText.replace(' ', '+')
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=5&shop_id=${shopId}&start=0&q=${text}`

  const etalaseId = +eId || 0
  if (etalaseId) {
    url += `&etalase=${etalaseId}`
  }
  return {
    type: FETCH_SEARCH_PRODUCT,
    payload: axios.get(url),
    queryText: queryText,
  }
}

export const ON_SEARCH_RESULT_TAP = 'ON_SEARCH_RESULT_TAP'
export const onSearchResultTap = () => {
  return {
    type: ON_SEARCH_RESULT_TAP,
  }
}

export const CLEAR_SEARCH_RESULTS = 'CLEAR_SEARCH_RESULTS'
export const clearSearchResults = () => {
  return {
    type: CLEAR_SEARCH_RESULTS,
  }
}

export const SET_SEARCH_TEXT = 'SET_SEARCH_TEXT'
export const setSearchText = (q) => {
  return {
    type: SET_SEARCH_TEXT,
    payload: q,
  }
}

export const ON_SUBMIT_FETCH_SEARCH_PRODUCT = 'ON_SUBMIT_FETCH_SEARCH_PRODUCT'
export const onSubmitFetchSearchProduct = (queryText, eId, shopId) => {
  const text = queryText.replace(' ', '+')
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=25&shop_id=${shopId}&start=0&q=${text}`
  const etalaseId = +eId || 0
  if (etalaseId) {
    url += `&etalase=${etalaseId}`
  }
  return {
    type: ON_SUBMIT_FETCH_SEARCH_PRODUCT,
    payload: axios.get(url),
    queryText: queryText,
  }
}