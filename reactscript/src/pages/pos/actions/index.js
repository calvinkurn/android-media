import axios from 'axios'

// Product list action and action creators
export const FETCH_PRODUCTS = 'FETCH_PRODUCTS'
export const fetchProducts = (shopId, start, rows, etalaseId) => {
  const eId = +etalaseId || 0
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=${rows}&shop_id=${shopId}&start=${start}`

  if (eId) {
    url += `&etalase=${eId}`
  }

  return {
    type: FETCH_PRODUCTS,
    payload: axios.get(url)
  }
}

export const FETCH_ETALASE = 'FETCH_ETALASE'
export const fetchEtalase = (shopId) => ({
  type: FETCH_ETALASE,
  payload: axios.get(`https://tome.tokopedia.com/v1/web-service/shop/get_etalase?shop_id=${shopId}`)
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

// Cart actions and action creators
export const ADD_TO_CART = 'ADD_TO_CART'
export const addToCart = (item) => {
  return {
    type: ADD_TO_CART,
    payload: item,
  }
}

export const REMOVE_FROM_CART = 'REMOVE_FROM_CART'
export const removeFromCart = (id) => {
  return {
    type: REMOVE_FROM_CART,
    payload: { id }
  }
}

export const INCREMENT_QTY = 'INCREMENT_QTY'
export const incrementQty = (id) => {
  return {
    type: INCREMENT_QTY,
    payload: { id }
  }
}

export const DECREMENT_QTY = 'DECREMENT_QTY'
export const decrementQty = (id) => {
  return {
    type: DECREMENT_QTY,
    payload: { id }
  }
}

export const CLEAR_CART = 'CLEAR_CART'
export const clearCart = () => {
  return {
    type: CLEAR_CART,
  }
}

export const BANK_SELECTED = 'BANK_SELECTED'
export const selectBank = (id) => {
  return {
    type: BANK_SELECTED,
    payload: id
  }
}

export const FETCH_BANK_FUlFILLED = 'FETCH_BANK_FUlFILLED'
export const getBankList = () => {
  return {
    type: FETCH_BANK_FUlFILLED,
  }
}


export const SELECT_PAYMENT_OPTIONS = 'SELECT_PAYMENT_OPTIONS'
export const selectPaymentOptions = (option, value) => {
  return {
    type: SELECT_PAYMENT_OPTIONS,
    payload: { option: option, value: value }
  }
}

export const FETCH_EMI_FUlFILLED = 'FETCH_EMI_FUlFILLED'
export const getEmiList = () => {
  return {
    type: FETCH_EMI_FUlFILLED,
  }
}

export const EMI_SELECTED = 'EMI_SELECTED'
export const selectEmi = (id) => {
  return {
    type: EMI_SELECTED,
    payload: id
  }
}

//Search actions
export const ON_SEARCH_QUERY_TYPE = 'ON_SEARCH_QUERY_TYPE'
export const onSearchQueryType = (queryText) => {
  return {
    type: ON_SEARCH_QUERY_TYPE,
    payload: queryText
  }
}

export const FETCH_SEARCH_PRODUCT = 'FETCH_SEARCH_PRODUCT'
export const fetchSearchProduct = (queryText) => {
  const text = queryText.replace(' ', '+')
  let url = `https://ace.tokopedia.com/search/product/v3.1?device=android&source=shop_product&ob=14&rows=5&shop_id=1987772&start=0&q=${text}`
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

export const FETCH_TRANSACTION_HISTORY = 'FETCH_TRANSACTION_HISTORY'
export const getTransactionHistory = () => {
  return {
    type: FETCH_TRANSACTION_HISTORY,
  }
}