import { combineReducers } from 'redux'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import { FETCH_DATA_DIGITAL } from '../actions'


const dataDigital = (state = {
    data: '',
    isFetching: false
}, action) => {
    switch (action.type){
        case `${FETCH_DATA_DIGITAL}_${PENDING}`:
            return {
                ...state,
                isFetching: true
            }

        case `${FETCH_DATA_DIGITAL}_${FULFILLED}`:
            console.log(action.payload)
            return {
                ...state,
                isFetching: false,
                data: action.payload.data
            }

        case `${FETCH_DATA_DIGITAL}_${REJECTED}`:
            return { 
                ...state,
                isFetching: false
            }


        default:
            return state        
    }
}


const rootReducer = combineReducers({
    dataDigital
})

export default rootReducer