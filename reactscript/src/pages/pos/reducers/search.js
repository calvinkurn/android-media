import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  SEARCH_PRODUCT,
  FETCH_SEARCH_PRODUCT,
  CLEAR_SEARCH_RESULTS,
  ON_SEARCH_QUERY_TYPE,
  SET_SEARCH_TEXT,
} from '../actions/index'




export const search = (state = {
  items: [],
  query: '',
  isFetching: false,
}, action) => {
  switch (action.type) {
    case ON_SEARCH_QUERY_TYPE:
      return {
        ...state,
        query: action.payload,
      }
    case `${SEARCH_PRODUCT}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${SEARCH_PRODUCT}_${FULFILLED}`:
      // console.log(action.payload)
      // console.log(action.payload.data.list)
      return {
        ...state,
        items: action.payload.data.list.map(p => ({
          id: p.product_id,
          text: p.product_name
        })),
        isFetching: false,
      }
    case `${SEARCH_PRODUCT}_${REJECTED}`:
      return {
        ...state,
        isFetching: false,
      }
    case CLEAR_SEARCH_RESULTS:
      return {
        items: [],
        query: '',
      }
    case SET_SEARCH_TEXT:
      // console.log(action.payload)
      return {
        ...state,
        query: action.payload
      }
    default:
      return state
  }
}


// export const search = (state = {
//     items: [],
//     query: '',
//     isFetching: false,
//   }, action) => {
//     switch (action.type) {
//       case ON_SEARCH_QUERY_TYPE:
//         return {
//           ...state,
//           query: action.payload,
//         }
//       case `${FETCH_SEARCH_PRODUCT}_${PENDING}`:
//         return {
//           ...state,
//           isFetching: true,
//         }
//       case `${FETCH_SEARCH_PRODUCT}_${FULFILLED}`:
//         return {
//           ...state,
//           items: action.payload.data.data.products.map(p => ({
//             id: p.id,
//             text: p.name
//           })),
//           isFetching: false,
//         }
//       case `${FETCH_SEARCH_PRODUCT}_${REJECTED}`:
//         return {
//           ...state,
//           isFetching: false,
//         }
//       case CLEAR_SEARCH_RESULTS:
//         return {
//           items: [],
//           query: '',
//         }
//       case SET_SEARCH_TEXT:
//         console.log(action.payload)
//         return {
//           ...state,
//           query: action.payload
//         }
//       default:
//         return state
//     }
//   }