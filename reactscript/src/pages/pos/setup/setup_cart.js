import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from '../store/Store'
import CartContainer from '../containers/CartContainer'


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <CartContainer onBackPress={() => {}}/>
            </Provider>
        )
    }
}
export default Root